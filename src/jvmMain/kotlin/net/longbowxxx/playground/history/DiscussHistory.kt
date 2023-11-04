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
import net.longbowxxx.generativeai.DiscussMessage
import net.longbowxxx.playground.utils.DebugLoggable
import net.longbowxxx.playground.utils.logInfo
import org.mongodb.kbson.BsonObjectId
import org.mongodb.kbson.ObjectId
import java.io.File

class DiscussHistory(appDataDir: File, dbFileDir: String = DB_DIR, dbFileName: String = DB_FILE_NAME) :
    RealmBase(appDataDir) {
    companion object {
        private const val DB_DIR = "db"
        private const val DB_FILE_NAME = "discuss-history.realm"
        private const val DEFAULT_SESSION_TITLE = "New Discuss"
        private const val SCHEME_VERSION = 0L
    }

    override val schema = setOf(DiscussHistoryData::class, DiscussMessageData::class)
    override val realmDirectory = dbFileDir
    override val realmFileName = dbFileName
    override val schemeVersion = SCHEME_VERSION
    override val migration = DiscussMigration()

    class DiscussMigration : AutomaticSchemaMigration, DebugLoggable {
        override fun migrate(migrationContext: AutomaticSchemaMigration.MigrationContext) {
            // schemaのバージョンが上がったら、マイグレーションコードを実装すること
            logInfo { "migrate() migrationContext=$migrationContext" }
        }
    }

    data class DiscussHistorySession(
        val id: ObjectId,
        var title: String,
        var categories: List<String>,
        var messages: List<DiscussMessage>,
        var updateAt: Long = System.currentTimeMillis(),
    ) {
        companion object {
            operator fun invoke(
                title: String,
                categories: List<String>,
                messages: List<DiscussMessage>,
            ): DiscussHistorySession {
                return DiscussHistorySession(BsonObjectId(), title, categories, messages)
            }

            operator fun invoke(): DiscussHistorySession {
                return DiscussHistorySession(BsonObjectId(), DEFAULT_SESSION_TITLE, emptyList(), emptyList())
            }
        }
    }

    suspend fun saveSession(session: DiscussHistorySession) {
        // 一度も対話していないものは記録しない
        val hasMessage = session.messages.any { it.content.isNotEmpty() }
        if (!hasMessage) {
            return
        }

        writeToRealm {
            val newData = session.toData()
            query<DiscussHistoryData>("id == $0", newData.id).first().find()
                ?.also { data ->
                    findLatest(data)?.title = newData.title
                    findLatest(data)?.categories = newData.categories
                    findLatest(data)?.messages = newData.messages
                } ?: copyToRealm(session.toData())
        }
    }

    suspend fun removeHistory(item: DiscussHistorySession) {
        writeToRealm {
            val deleteData = item.toData()
            val deleteQuery = query<DiscussHistoryData>("id == $0", deleteData.id)
            delete(deleteQuery)
        }
    }

    suspend fun clearHistory() {
        writeToRealm {
            deleteAll()
        }
    }

    suspend fun getHistory(): List<DiscussHistorySession> {
        return readFromRealm {
            query<DiscussHistoryData>().sort("updateAt", Sort.DESCENDING).find().toList().map {
                it.toSession()
            }
        }
    }

    private fun DiscussHistorySession.toData(): DiscussHistoryData {
        return DiscussHistoryData().apply {
            id = this@toData.id
            title = this@toData.title
            categories = this@toData.categories.toRealmList()
            messages = this@toData.messages.map { it.toData() }.toRealmList()
            updateAt = this@toData.updateAt
        }
    }

    private fun DiscussHistoryData.toSession(): DiscussHistorySession {
        return DiscussHistorySession(
            id,
            title,
            categories.toList(),
            messages.map { it.toSession() }.toList(),
            updateAt,
        )
    }

    private fun DiscussMessage.toData(): DiscussMessageData {
        return DiscussMessageData().apply {
            author = this@toData.author
            content = this@toData.content
        }
    }

    private fun DiscussMessageData.toSession() =
        DiscussMessage(author, content)

    class DiscussHistoryData : RealmObject {
        @PrimaryKey
        var id: ObjectId = BsonObjectId()

        @Index
        var updateAt: Long = System.currentTimeMillis()
        var title: String = ""
        var categories: RealmList<String> = realmListOf()
        var messages: RealmList<DiscussMessageData> = realmListOf()
    }

    class DiscussMessageData : RealmObject {
        var author: String = ""
        var content: String = ""
    }
}
