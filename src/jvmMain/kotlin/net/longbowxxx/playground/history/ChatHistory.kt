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
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import net.longbowxxx.openai.client.OpenAiChatMessage
import net.longbowxxx.openai.client.OpenAiChatRoleTypes
import org.mongodb.kbson.ObjectId

class ChatHistory : RealmBase() {
    companion object {
        private const val DB_DIR = "db"
        private const val DB_FILE_NAME = "chat-history.realm"
    }

    override val schema = setOf(ChatHistoryData::class, ChatMessageData::class)
    override val realmDirectory = DB_DIR
    override val realmFileName = DB_FILE_NAME

    data class ChatHistoryItem(
        val id: ObjectId? = null,
        var title: String,
        var categories: List<String>,
        var messages: List<OpenAiChatMessage>,
    ) {
        companion object {
            operator fun invoke(
                title: String,
                categories: List<String>,
                messages: List<OpenAiChatMessage>,
            ): ChatHistoryItem {
                return ChatHistoryItem(null, title, categories, messages)
            }
        }
    }

    suspend fun addHistory(item: ChatHistoryItem) {
        writeToRealm {
            copyToRealm(item.toData())
        }
    }

    suspend fun updateHistory(item: ChatHistoryItem) {
        requireNotNull(item.id) { "ChatHistoryItem id must not be null." }
        writeToRealm {
            val newData = item.toData()
            query<ChatHistoryData>("id == $0", newData.id).first().find()?.also { data ->
                findLatest(data)?.title = newData.title
                findLatest(data)?.categories = newData.categories
                findLatest(data)?.messages = newData.messages
            }
        }
    }

    suspend fun removeHistory(item: ChatHistoryItem) {
        requireNotNull(item.id) { "ChatHistoryItem id must not be null." }
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

    suspend fun getHistory(): List<ChatHistoryItem> {
        return readFromRealm {
            query<ChatHistoryData>().find().toList().map {
                it.toItem()
            }
        }
    }

    private fun ChatHistoryItem.toData(): ChatHistoryData {
        return ChatHistoryData().apply {
            id = this@toData.id ?: ObjectId()
            title = this@toData.title
            categories = this@toData.categories.toRealmList()
            messages = this@toData.messages.map { it.toData() }.toRealmList()
        }
    }

    private fun ChatHistoryData.toItem(): ChatHistoryItem {
        return ChatHistoryItem(
            id,
            title,
            categories.toList(),
            messages.map { it.toItem() }.toList(),
        )
    }

    private fun OpenAiChatMessage.toData(): ChatMessageData {
        return ChatMessageData().apply {
            role = this@toData.role.toInt()
            content = this@toData.content
            name = this@toData.name
        }
    }

    private fun ChatMessageData.toItem() = OpenAiChatMessage(role.toOpenAiChatRoleTypes(), content, name)

    class ChatHistoryData : RealmObject {
        @PrimaryKey
        var id: ObjectId = ObjectId()
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
