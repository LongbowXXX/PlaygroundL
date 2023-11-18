/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.history

import io.realm.kotlin.ext.query
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.ext.toRealmList
import io.realm.kotlin.migration.AutomaticSchemaMigration
import io.realm.kotlin.query.Sort
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.Index
import io.realm.kotlin.types.annotations.PrimaryKey
import net.longbowxxx.openai.client.OpenAiChatFunctionCallMessage
import net.longbowxxx.openai.client.OpenAiChatMessage
import net.longbowxxx.openai.client.OpenAiChatRoleTypes
import net.longbowxxx.playground.utils.DebugLoggable
import net.longbowxxx.playground.utils.logInfo
import org.mongodb.kbson.ObjectId
import java.io.File

/**
 * Manage chat history with Realm.
 *
 * @constructor Create chat history.
 * @param appDataDir Application data directory.
 * @param dbFileDir Directory name to save Realm file.
 * @param dbFileName Realm file name.
 */
class ChatHistory(appDataDir: File, dbFileDir: String = DB_DIR, dbFileName: String = DB_FILE_NAME) :
    RealmBase(appDataDir) {
    companion object {
        private const val DB_DIR = "db"
        private const val DB_FILE_NAME = "chat-history.realm"
        private const val DEFAULT_SESSION_TITLE = "New chat"
        private const val SCHEME_VERSION = 2L
    }

    override val schema = setOf(ChatHistoryData::class, ChatMessageData::class, ChatFunctionCallData::class)
    override val realmDirectory = dbFileDir
    override val realmFileName = dbFileName
    override val schemeVersion = SCHEME_VERSION
    override val migration = ChatMigration()

    class ChatMigration : AutomaticSchemaMigration, DebugLoggable {
        override fun migrate(migrationContext: AutomaticSchemaMigration.MigrationContext) {
            // if schema version is updated, implement migration code
            logInfo { "migrate() migrationContext=$migrationContext" }
        }
    }

    data class ChatHistorySession(
        val id: ObjectId,
        var title: String,
        var categories: List<String>,
        var messages: List<OpenAiChatMessage>,
        var updateAt: Long = System.currentTimeMillis(),
    ) {
        companion object {
            operator fun invoke(
                title: String,
                categories: List<String>,
                messages: List<OpenAiChatMessage>,
            ): ChatHistorySession {
                return ChatHistorySession(ObjectId(), title, categories, messages)
            }

            operator fun invoke(): ChatHistorySession {
                return ChatHistorySession(ObjectId(), DEFAULT_SESSION_TITLE, emptyList(), emptyList())
            }
        }
    }

    suspend fun saveSession(session: ChatHistorySession) {
        // if there is no message from assistant, do not save
        val hasAssistantMessage = session.messages.any { it.role == OpenAiChatRoleTypes.ASSISTANT }
        if (!hasAssistantMessage) {
            return
        }

        writeToRealm {
            val newData = session.toData()
            query<ChatHistoryData>("id == $0", newData.id).first().find()
                ?.also { data ->
                    findLatest(data)?.title = newData.title
                    findLatest(data)?.categories = newData.categories
                    findLatest(data)?.messages = newData.messages
                } ?: copyToRealm(session.toData())
        }
    }

    suspend fun removeHistory(item: ChatHistorySession) {
        writeToRealm {
            val deleteData = item.toData()
            val deleteQuery = query<ChatHistoryData>("id == $0", deleteData.id)
            delete(deleteQuery)
        }
    }

    suspend fun clearHistory() {
        writeToRealm {
            deleteAll()
        }
    }

    suspend fun getHistory(): List<ChatHistorySession> {
        return readFromRealm {
            query<ChatHistoryData>().sort("updateAt", Sort.DESCENDING).find().toList().map {
                it.toSession()
            }
        }
    }

    private fun ChatHistorySession.toData(): ChatHistoryData {
        return ChatHistoryData().apply {
            id = this@toData.id
            title = this@toData.title
            categories = this@toData.categories.toRealmList()
            messages = this@toData.messages.map { it.toData() }.toRealmList()
            updateAt = this@toData.updateAt
        }
    }

    private fun ChatHistoryData.toSession(): ChatHistorySession {
        return ChatHistorySession(
            id,
            title,
            categories.toList(),
            messages.map { it.toSession() }.toList(),
            updateAt,
        )
    }

    private fun OpenAiChatMessage.toData(): ChatMessageData {
        return ChatMessageData().apply {
            role = this@toData.role.toInt()
            content = this@toData.content
            functionCall = this@toData.functionCall?.toChatFunctionCallData()
            name = this@toData.name
        }
    }

    private fun ChatMessageData.toSession() =
        OpenAiChatMessage(role.toOpenAiChatRoleTypes(), content, functionCall?.toOpenAiChatFunctionCall(), name)

    class ChatHistoryData : RealmObject {
        @PrimaryKey
        var id: ObjectId = ObjectId()

        @Index
        var updateAt: Long = System.currentTimeMillis()
        var title: String = ""
        var categories: RealmList<String> = realmListOf()
        var messages: RealmList<ChatMessageData> = realmListOf()
    }

    class ChatMessageData : RealmObject {
        var role: Int = 0
        var content: String? = null
        var functionCall: ChatFunctionCallData? = null
        var name: String? = null
    }

    class ChatFunctionCallData : RealmObject {
        var name: String = ""
        var arguments: String = ""
    }

    private fun OpenAiChatRoleTypes.toInt(): Int = this.ordinal

    private fun Int.toOpenAiChatRoleTypes(): OpenAiChatRoleTypes = OpenAiChatRoleTypes.values()[this]

    private fun ChatFunctionCallData.toOpenAiChatFunctionCall(): OpenAiChatFunctionCallMessage =
        OpenAiChatFunctionCallMessage(name, arguments)

    private fun OpenAiChatFunctionCallMessage.toChatFunctionCallData() =
        ChatFunctionCallData().apply {
            name = this@toChatFunctionCallData.name
            arguments = this@toChatFunctionCallData.arguments
        }
}
