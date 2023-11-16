/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.viewmodel

import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.longbowxxx.generativeai.DISCUSS_MODEL
import net.longbowxxx.generativeai.DiscussMessage
import net.longbowxxx.generativeai.DiscussPrompt
import net.longbowxxx.generativeai.DiscussRequest
import net.longbowxxx.generativeai.GenerativeAiClient
import net.longbowxxx.generativeai.GenerativeAiSettings
import net.longbowxxx.playground.history.DiscussHistory
import net.longbowxxx.playground.logger.DiscussLogger
import net.longbowxxx.playground.utils.appDataDirectory
import java.io.Closeable
import java.io.File
import kotlin.coroutines.CoroutineContext

/**
 * ViewModel of Google Palm2 Discuss function.
 */
class DiscussViewModel(dispatcher: CoroutineDispatcher = Dispatchers.Default) : CoroutineScope, Closeable {
    private val job = Job()
    override val coroutineContext: CoroutineContext = dispatcher + job

    companion object {
        const val USER_AUTHOR = "0"
        private val INITIAL_MESSAGES = listOf(DiscussMessage(USER_AUTHOR, ""))
    }

    val messages = mutableStateOf(INITIAL_MESSAGES)
    val errorMessage = mutableStateOf("")
    val requesting = mutableStateOf(false)
    val history = mutableStateOf<List<DiscussHistory.DiscussHistorySession>>(emptyList())
    val models =
        listOf(
            DISCUSS_MODEL,
        )
    private var currentChatSession = DiscussHistory.DiscussHistorySession()

    val chatPromptFileList: List<File>
        get() {
            return File("chatPrompt").walkTopDown().filter {
                it.isFile && (it.name.endsWith(".md") || it.name.endsWith(".txt"))
            }.toList()
        }
    private var currentRequestJob: Job? = null

    val chatMessageFileList: List<File>
        get() {
            return File("chatMessage").walkTopDown().filter {
                it.isFile && (it.name.endsWith(".md") || it.name.endsWith(".txt"))
            }.toList()
        }

    fun updateHistory() {
        launch {
            val newHistory = discussHistory.getHistory()
            history.value = newHistory
        }
    }

    fun removeHistory(session: DiscussHistory.DiscussHistorySession) {
        launch {
            discussHistory.removeHistory(session)
            history.value = discussHistory.getHistory()
        }
    }

    fun updateMessage(
        index: Int,
        message: DiscussMessage,
    ) {
        val newList = mutableListOf<DiscussMessage>()
        newList.addAll(messages.value)
        require(index < newList.size)
        newList[index] = message
        messages.value = newList
    }

    fun removeMessage(index: Int) {
        val newList = mutableListOf<DiscussMessage>()
        newList.addAll(messages.value)
        require(index < newList.size)
        newList.removeAt(index)
        messages.value = newList
        currentChatSession.messages = newList
    }

    fun addMessage(
        author: String,
        content: String = "",
    ): Int {
        val newList = mutableListOf<DiscussMessage>()
        newList.addAll(messages.value)
        newList.add(DiscussMessage(author, content))
        messages.value = newList
        currentChatSession.messages = newList
        return newList.size - 1
    }

    fun newSession() {
        messages.value = INITIAL_MESSAGES
        val lastSession = currentChatSession
        launch {
            discussHistory.saveSession(lastSession)
        }
        currentChatSession = DiscussHistory.DiscussHistorySession()
    }

    fun requestChat() {
        val lastJob = currentRequestJob
        lastJob?.cancel()
        currentRequestJob =
            launch {
                lastJob?.join()
                // 古いエラーを消す
                errorMessage.value = ""

                val currentModel = discussProperties.discussModel.value
                requesting.value = true
                val session = currentChatSession
                val logger = DiscussLogger(appDataDirectory)
                runCatching {
                    val request =
                        DiscussRequest(
                            currentModel,
                            prompt =
                                DiscussPrompt(
                                    messages.value,
                                    examples = emptyList(),
                                    context = discussProperties.discussContext.value,
                                ),
                            temperature = discussProperties.discussTemperature.value,
                            candidateCount = 1,
                        )
                    val client = GenerativeAiClient(GenerativeAiSettings(appProperties.palmApiKey))
                    val response = client.requestDiscuss(request)
                    val newMessage = response.candidates.first()
                    addMessage(newMessage.author, newMessage.content)

                    val latestMessages = messages.value
                    session.messages = latestMessages
                    logger.logRequest(request)
                    logger.logMessages(latestMessages)
                    launch {
                        updateDiscussSessionTitle(latestMessages, session)
                    }
                    // レスポンスが終わったら、次の入力用のメッセージ追加
                    addMessage(USER_AUTHOR)
                }.onFailure {
                    errorMessage.value = it.message ?: it.toString()
                    logger.logError(it)
                }.also {
                    logger.close()
                    requesting.value = false
                }
            }
    }

    fun restoreOldSession(session: DiscussHistory.DiscussHistorySession) {
        val lastSession = currentChatSession
        launch {
            discussHistory.saveSession(lastSession)
        }
        currentChatSession = session
        messages.value = session.messages
    }

    override fun close() {
        runBlocking {
            job.cancelAndJoin()
            discussHistory.saveSession(currentChatSession)
        }
    }
}
