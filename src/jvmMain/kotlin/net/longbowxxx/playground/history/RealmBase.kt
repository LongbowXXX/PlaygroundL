/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.history

import io.realm.kotlin.MutableRealm
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.types.BaseRealmObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.Closeable
import kotlin.reflect.KClass

abstract class RealmBase : Closeable, CoroutineScope {
    @OptIn(DelicateCoroutinesApi::class)
    private val dispatcher = newSingleThreadContext("sqlite-thread")
    private val job = Job()
    override val coroutineContext = job + dispatcher

    private var realm: Realm? = null
    protected abstract val schema: Set<KClass<out BaseRealmObject>>
    protected abstract val realmDirectory: String
    protected abstract val realmFileName: String
    protected abstract val schemeVersion: Long

    protected suspend fun <R> readFromRealm(block: Realm.() -> R): R {
        return withContext(coroutineContext) {
            val realmInstance = realm ?: openRealm()
            realmInstance.block()
        }
    }

    protected suspend fun <R> writeToRealm(block: MutableRealm.() -> R): R {
        return withContext(coroutineContext) {
            val realmInstance = realm ?: openRealm()
            realmInstance.write(block)
        }
    }

    private fun openRealm(): Realm {
        val configuration = RealmConfiguration.Builder(schema)
            .schemaVersion(schemeVersion)
            .directory(realmDirectory)
            .name(realmFileName)
            .build()
        return Realm.open(configuration)
    }

    override fun close() {
        runBlocking {
            job.cancelAndJoin()
            dispatcher.close()
        }
    }
}
