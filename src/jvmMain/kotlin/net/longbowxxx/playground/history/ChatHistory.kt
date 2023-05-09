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
import io.realm.kotlin.query.Sort
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.Index
import io.realm.kotlin.types.annotations.PrimaryKey
import net.longbowxxx.openai.client.OpenAiChatMessage
import net.longbowxxx.openai.client.OpenAiChatRoleTypes
import org.mongodb.kbson.ObjectId

class ChatHistory : RealmBase() {
    companion object {
        private const val DB_DIR = "db"
        private const val DB_FILE_NAME = "chat-history.realm"
        private const val DEFAULT_SESSION_TITLE = "New chat"
        private const val SCHEME_VERSION = 0L
    }

    override val schema = setOf(ChatHistoryData::class, ChatMessageData::class)
    override val realmDirectory = DB_DIR
    override val realmFileName = DB_FILE_NAME
    override val schemeVersion = SCHEME_VERSION

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
        // 一度も対話していないものは記録しない
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
            name = this@toData.name
        }
    }

    private fun ChatMessageData.toSession() = OpenAiChatMessage(role.toOpenAiChatRoleTypes(), content, name)

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
        var content: String = ""
        var name: String? = null
    }

    private fun OpenAiChatRoleTypes.toInt(): Int = this.ordinal
    private fun Int.toOpenAiChatRoleTypes(): OpenAiChatRoleTypes = OpenAiChatRoleTypes.values()[this]
}
