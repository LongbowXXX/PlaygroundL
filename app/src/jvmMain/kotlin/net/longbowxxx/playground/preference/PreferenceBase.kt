/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.preference

import java.io.Closeable
import java.io.File
import java.util.Properties
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

/**
 * Preference base class.
 *
 * @constructor Create preference base class.
 * @param appDataDir Application data directory.
 */
@OptIn(ExperimentalEncodingApi::class)
abstract class PreferenceBase(
    private val appDataDir: File,
) : Closeable {
    protected val properties = Properties()
    protected abstract val fileName: String
    protected abstract val fileComment: String

    /**
     * Load preference.
     */
    abstract fun load()

    /**
     * Load preference with block.
     *
     * @param block Block to load preference.
     */
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

    /**
     * Save preference.
     */
    abstract fun save()

    /**
     * Save preference with block.
     *
     * @param block Block to save preference.
     */
    protected fun saveInternal(block: Properties.() -> Unit) {
        properties.block()
        val file = File(appDataDir, fileName)
        file.writer(Charsets.UTF_8).use { writer ->
            properties.store(writer, fileComment)
        }
    }

    /**
     * Get float property.
     *
     * @param key Property key.
     * @param defaultValue Default value.
     */
    protected fun Properties.getFloatProperty(
        key: String,
        defaultValue: Float,
    ): Float {
        return if (containsKey(key)) {
            getProperty(key).toFloatOrNull() ?: defaultValue
        } else {
            defaultValue
        }
    }

    /**
     * Get int property.
     *
     * @param key Property key.
     * @param defaultValue Default value.
     */
    protected fun Properties.getIntProperty(
        key: String,
        defaultValue: Int,
    ): Int {
        return if (containsKey(key)) {
            getProperty(key).toIntOrNull() ?: defaultValue
        } else {
            defaultValue
        }
    }

    /**
     * Get secret property.
     *
     * @param key Property key.
     * @param defaultValue Default value.
     */
    @Suppress("SameParameterValue")
    protected fun Properties.getOrDefaultSecretProperty(
        key: String,
        defaultValue: String,
    ): String {
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

    /**
     * Set secret property.
     *
     * @param key Property key.
     * @param value Property value.
     */
    @Suppress("SameParameterValue")
    protected fun Properties.setSecretProperty(
        key: String,
        value: String,
    ) {
        if (value.isNotEmpty()) {
            val encryptedValue = encrypt(value)
            setProperty(key, encryptedValue)
        } else {
            remove(key)
        }
    }

    private fun salt(): String {
        val output =
            Runtime.getRuntime().exec("wmic baseboard get serialnumber")
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
