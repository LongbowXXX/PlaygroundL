/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.preference

import java.io.Closeable
import java.io.File
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@OptIn(ExperimentalEncodingApi::class)
abstract class PreferenceBase(
    private val appDataDir: File,
) : Closeable {
    protected val properties = Properties()
    protected abstract val fileName: String
    protected abstract val fileComment: String

    abstract fun load()
    protected fun loadInternal(block: Properties.() -> Unit) {
        runCatching {
            val file = File(appDataDir, fileName)
            file.reader(Charsets.UTF_8).use { reader ->
                properties.load(reader)
                properties.block()
            }
        }.onFailure {
            save()
        }
    }

    abstract fun save()
    protected fun saveInternal(block: Properties.() -> Unit) {
        properties.block()
        val file = File(appDataDir, fileName)
        file.writer(Charsets.UTF_8).use { writer ->
            properties.store(writer, fileComment)
        }
    }

    protected fun Properties.getFloatProperty(key: String, defaultValue: Float): Float {
        return if (containsKey(key)) {
            getProperty(key).toFloatOrNull() ?: defaultValue
        } else {
            defaultValue
        }
    }

    protected fun Properties.getIntProperty(key: String, defaultValue: Int): Int {
        return if (containsKey(key)) {
            getProperty(key).toIntOrNull() ?: defaultValue
        } else {
            defaultValue
        }
    }

    @Suppress("SameParameterValue")
    protected fun Properties.getOrDefaultSecretProperty(key: String, defaultValue: String): String {
        return if (containsKey(key)) {
            val encryptedValue = getProperty(key)
            runCatching {
                decrypt(encryptedValue)
            }.onFailure {
                remove(key)
            }.getOrDefault(defaultValue)
        } else {
            defaultValue
        }
    }

    @Suppress("SameParameterValue")
    protected fun Properties.setSecretProperty(key: String, value: String) {
        if (value.isNotEmpty()) {
            val encryptedValue = encrypt(value)
            setProperty(key, encryptedValue)
        } else {
            remove(key)
        }
    }

    private fun salt(): String {
        val output = Runtime.getRuntime().exec("wmic baseboard get serialnumber")
            .inputStream.bufferedReader().readText().trim()
        val a = output.substringAfterLast(" ").hashCode()
        val b = System.getProperty("user.dir").hashCode()
        val c = System.getenv("COMPUTERNAME").hashCode()
        val d = System.getProperty("user.name").hashCode()
        return a.toHex() + b.toHex() + c.toHex() + d.toHex()
    }

    private fun Int.toHex(): String {
        return String.format("%08x", this)
    }

    private fun encrypt(data: String): String {
        val secretKey = SecretKeySpec(salt().toByteArray(Charsets.UTF_8), "AES")
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        return Base64.encode(cipher.doFinal(data.toByteArray(Charsets.UTF_8)))
    }

    private fun decrypt(strData: String): String {
        val data = Base64.decode(strData)
        val secretKey = SecretKeySpec(salt().toByteArray(Charsets.UTF_8), "AES")
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.DECRYPT_MODE, secretKey)
        return String(cipher.doFinal(data), Charsets.UTF_8)
    }

    override fun close() {
        save()
    }
}
