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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.longbowxxx.openai.client.OPENAI_CHAT_MODEL_GPT_35_TURBO
import net.longbowxxx.openai.client.OPENAI_CHAT_MODEL_GPT_4
import net.longbowxxx.openai.client.OPENAI_CHAT_URL
import net.longbowxxx.openai.client.OpenAiChatMessage
import net.longbowxxx.openai.client.OpenAiChatRequest
import net.longbowxxx.openai.client.OpenAiChatRoleTypes
import net.longbowxxx.openai.client.OpenAiChatStreamResponse
import net.longbowxxx.openai.client.OpenAiClient
import net.longbowxxx.openai.client.OpenAiSettings
import net.longbowxxx.playground.logger.ChatLogger
import net.longbowxxx.playground.logger.LOG_DIR
import java.io.Closeable
import java.io.File
import kotlin.coroutines.CoroutineContext

class ChatViewModel(dispatcher: CoroutineDispatcher = Dispatchers.Default) : CoroutineScope, Closeable {
    private val job = Job()
    override val coroutineContext: CoroutineContext = dispatcher + job

    companion object {
        private val INITIAL_MESSAGES = listOf(OpenAiChatMessage(OpenAiChatRoleTypes.USER, ""))
    }

    val messages = mutableStateOf(INITIAL_MESSAGES)
    val errorMessage = mutableStateOf("")
    val requesting = mutableStateOf(false)
    val models = listOf(OPENAI_CHAT_MODEL_GPT_35_TURBO, OPENAI_CHAT_MODEL_GPT_4)
    val chatPromptFileList: List<File>
        get() {
            return File("chatPrompt").walkTopDown().filter {
                it.isFile && (it.name.endsWith(".md") || it.name.endsWith(".txt"))
            }.toList()
        }

    fun updateMessage(index: Int, message: OpenAiChatMessage) {
        val newList = mutableListOf<OpenAiChatMessage>()
        newList.addAll(messages.value)
        require(index < newList.size)
        newList[index] = message
        messages.value = newList
    }

    fun toggleRole(index: Int) {
        val newList = mutableListOf<OpenAiChatMessage>()
        newList.addAll(messages.value)
        require(index < newList.size)
        newList[index] = newList[index].toggleRole()
        messages.value = newList
    }

    fun removeMessage(index: Int) {
        val newList = mutableListOf<OpenAiChatMessage>()
        newList.addAll(messages.value)
        require(index < newList.size)
        newList.removeAt(index)
        messages.value = newList
    }

    fun addMessage(): Int {
        val newList = mutableListOf<OpenAiChatMessage>()
        newList.addAll(messages.value)
        newList.add(OpenAiChatMessage(OpenAiChatRoleTypes.USER, ""))
        messages.value = newList
        return newList.size - 1
    }

    fun clearMessages() {
        messages.value = INITIAL_MESSAGES
    }

    fun requestChat() {
        launch {
            // 古いエラーを消す
            errorMessage.value = ""

            requesting.value = true
            val logger = ChatLogger(LOG_DIR)
            runCatching {
                val request = OpenAiChatRequest(
                    chatProperties.chatModel.value,
                    messages = createMessages(),
                    stream = true,
                    temperature = chatProperties.chatTemperature.value,
                    topP = chatProperties.chatTopP.value,
                    maxTokens = chatProperties.chatMaxTokens.value,
                    presencePenalty = chatProperties.chatPresencePenalty.value,
                    frequencyPenalty = chatProperties.chatFrequencyPenalty.value,
                )
                val client = OpenAiClient(OpenAiSettings(OPENAI_CHAT_URL, appProperties.apiKey))
                client.requestChatWithStreaming(request).correctStreamResponse()

                logger.logRequest(request)
                logger.logMessages(createMessages())
                // レスポンスが終わったら、次の入力用のメッセージ追加
                addMessage()
            }.onFailure {
                errorMessage.value = it.message ?: it.toString()
                logger.logError(it)
            }.also {
                logger.close()
                requesting.value = false
            }
        }
    }

    private suspend fun Flow<OpenAiChatStreamResponse>.correctStreamResponse() {
        var firstTime = true
        var itemIndex = 0
        this.collect { streamResponse ->
            if (firstTime) {
                firstTime = false
                itemIndex = addMessage()
            }

            streamResponse.choices.firstOrNull()?.delta?.content?.let { contentDelta ->
                val oldMessage = messages.value[itemIndex]
                val newContent = oldMessage.content + contentDelta
                updateMessage(itemIndex, OpenAiChatMessage(OpenAiChatRoleTypes.ASSISTANT, newContent))
            }
        }
    }

    private fun createMessages(): List<OpenAiChatMessage> {
        val messageList = mutableListOf<OpenAiChatMessage>()
        val system = chatProperties.chatSystemPrompt.value
        if (system.isNotEmpty()) {
            messageList.add(OpenAiChatMessage(OpenAiChatRoleTypes.SYSTEM, system))
        }
        messageList.addAll(messages.value)
        return messageList
    }

    private fun OpenAiChatMessage.toggleRole(): OpenAiChatMessage {
        return when (this.role) {
            OpenAiChatRoleTypes.ASSISTANT -> OpenAiChatMessage(OpenAiChatRoleTypes.USER, this.content)
            OpenAiChatRoleTypes.USER -> OpenAiChatMessage(OpenAiChatRoleTypes.ASSISTANT, this.content)
            else -> error("should not reach here")
        }
    }

    override fun close() {
        runBlocking {
            job.cancelAndJoin()
        }
    }
}