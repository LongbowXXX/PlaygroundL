/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.history

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import net.longbowxxx.openai.client.OpenAiChatMessage
import java.io.Closeable
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement

class ChatHistory : Closeable, CoroutineScope {
    companion object {
        private const val DB_FILE_NAME = "chat-database.db"
        private const val HISTORY_TABLE_NAME = "history-table"
        private const val ID_KEY = "id"
        private const val TITLE_KEY = "title"
        private const val CATEGORIES_KEY = "categories"
        private const val MESSAGES_KEY = "messages"
    }

    @OptIn(DelicateCoroutinesApi::class)
    private val dispatcher = newSingleThreadContext("sqlite-thread")
    private var connection: Connection? = null
    private val job = Job()
    override val coroutineContext = job + dispatcher

    suspend fun addHistory(item: ChatHistoryItem) {
        createTableIfNeeded()
        dbStatement {
            // executeUpdate("INSERT INTO your_table (data) VALUES ('$data')")
        }
    }

    suspend fun getHistory(): List<ChatHistoryItem> {
        createTableIfNeeded()
        return runQuery("SELECT * FROM $HISTORY_TABLE_NAME") { resultSet ->
            resultSet.toChatHistoryItem()
        }
    }

    private fun ResultSet.toChatHistoryItem() = ChatHistoryItem(
        getString(TITLE_KEY),
        getString(CATEGORIES_KEY),
        getString(MESSAGES_KEY)
    )

    private suspend fun createTableIfNeeded() {
        runUpdate {
            """
                CREATE TABLE IF NOT EXISTS $HISTORY_TABLE_NAME (
                    $ID_KEY INTEGER PRIMARY KEY AUTOINCREMENT,
                    $TITLE_KEY TEXT NOT NULL
                    $CATEGORIES_KEY TEXT NOT NULL
                    $MESSAGES_KEY TEXT NOT NULL
                )
            """.trimIndent()
        }
    }

    private suspend fun <T> runQuery(sql: String, transform: (ResultSet) -> T): List<T> {
        return dbStatement {
            executeQuery(sql)
                .map(transform)
        }
    }

    private suspend fun runUpdate(sqlBlock: () -> String): Int {
        return dbStatement {
            executeUpdate(sqlBlock())
        }
    }

    private inline fun <T> ResultSet.map(transform: (ResultSet) -> T): List<T> {
        use { resultSet ->
            val resultList = mutableListOf<T>()
            while (resultSet.next()) {
                resultList.add(transform(resultSet))
            }
            return resultList
        }
    }

    private suspend fun <T> dbStatement(block: Statement.() -> T): T {
        return withContext(coroutineContext) {
            val url = "jdbc:sqlite:$DB_FILE_NAME"
            val con = connection ?: DriverManager.getConnection(url)
            con.createStatement().run {
                block()
            }.also {
                close()
            }
        }
    }

    override fun close() {
        runBlocking {
            connection?.close()
            job.cancelAndJoin()
            dispatcher.close()
        }
    }
}

@Serializable
data class ChatHistoryItem(
    val title: String,
    val categories: List<String>,
    val messages: List<OpenAiChatMessage>,
) {
    companion object {
        private const val CATEGORY_DELIMITER=","
        operator fun invoke(title: String, categories: String, messages: String): ChatHistoryItem {

            ChatHistoryItem(
                title,
                categories.split(CATEGORY_DELIMITER),

            )
        }
    }
}
