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
import net.longbowxxx.openai.client.OPENAI_CHAT_MODEL_GPT_35_TURBO_0613
import net.longbowxxx.openai.client.OPENAI_CHAT_MODEL_GPT_4
import net.longbowxxx.openai.client.OPENAI_CHAT_MODEL_GPT_4_0613
import net.longbowxxx.openai.client.OpenAiChatFunctionCallMessage
import net.longbowxxx.openai.client.OpenAiChatFunctionCallMessageDelta
import net.longbowxxx.openai.client.OpenAiChatMessage
import net.longbowxxx.openai.client.OpenAiChatRequest
import net.longbowxxx.openai.client.OpenAiChatRoleTypes
import net.longbowxxx.openai.client.OpenAiChatStreamDelta
import net.longbowxxx.openai.client.OpenAiChatStreamResponse
import net.longbowxxx.openai.client.OpenAiClient
import net.longbowxxx.openai.client.OpenAiSettings
import net.longbowxxx.openai.client.isFunctionAvailable
import net.longbowxxx.playground.function.ChatFunctionLoader
import net.longbowxxx.playground.history.ChatHistory
import net.longbowxxx.playground.logger.ChatLogger
import java.io.Closeable
import java.io.File
import kotlin.coroutines.CoroutineContext

class ChatViewModel(dispatcher: CoroutineDispatcher = Dispatchers.Default) : CoroutineScope, Closeable {
    private val functionLoader = ChatFunctionLoader()
    private val job = Job()
    override val coroutineContext: CoroutineContext = dispatcher + job

    companion object {
        private val INITIAL_MESSAGES = listOf(OpenAiChatMessage(OpenAiChatRoleTypes.USER, "", null, null))
    }

    val messages = mutableStateOf(INITIAL_MESSAGES)
    val errorMessage = mutableStateOf("")
    val requesting = mutableStateOf(false)
    val history = mutableStateOf<List<ChatHistory.ChatHistorySession>>(emptyList())
    val models = listOf(
        OPENAI_CHAT_MODEL_GPT_35_TURBO_0613,
        OPENAI_CHAT_MODEL_GPT_35_TURBO,
        OPENAI_CHAT_MODEL_GPT_4_0613,
        OPENAI_CHAT_MODEL_GPT_4,
    )
    private var currentChatSession = ChatHistory.ChatHistorySession()

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
            val newHistory = chatHistory.getHistory()
            history.value = newHistory
        }
    }

    fun removeHistory(session: ChatHistory.ChatHistorySession) {
        launch {
            chatHistory.removeHistory(session)
            history.value = chatHistory.getHistory()
        }
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
        currentChatSession.messages = newList
    }

    fun removeMessage(index: Int) {
        val newList = mutableListOf<OpenAiChatMessage>()
        newList.addAll(messages.value)
        require(index < newList.size)
        newList.removeAt(index)
        messages.value = newList
        currentChatSession.messages = newList
    }

    fun addMessage(role: OpenAiChatRoleTypes, name: String? = null): Int {
        val newList = mutableListOf<OpenAiChatMessage>()
        newList.addAll(messages.value)
        newList.add(OpenAiChatMessage(role, null, null, name))
        messages.value = newList
        currentChatSession.messages = newList
        return newList.size - 1
    }

    fun newSession() {
        messages.value = INITIAL_MESSAGES
        val lastSession = currentChatSession
        launch {
            chatHistory.saveSession(lastSession)
        }
        currentChatSession = ChatHistory.ChatHistorySession()
    }

    fun requestChat() {
        val lastJob = currentRequestJob
        lastJob?.cancel()
        currentRequestJob = launch {
            lastJob?.join()
            // 古いエラーを消す
            errorMessage.value = ""

            val currentModel = chatProperties.chatModel.value
            val functionEnabled = currentModel.isFunctionAvailable()

            requesting.value = true
            val session = currentChatSession
            val logger = ChatLogger()
            val functions = if (functionEnabled) {
                functionLoader.loadFunctions(File("chatFunction"))
            } else {
                null
            }
            runCatching {
                val request = OpenAiChatRequest(
                    currentModel,
                    messages = createMessages(),
                    functions = functions,
                    stream = true,
                    temperature = chatProperties.chatTemperature.value,
                    topP = chatProperties.chatTopP.value,
                    maxTokens = chatProperties.chatMaxTokens.value,
                    presencePenalty = chatProperties.chatPresencePenalty.value,
                    frequencyPenalty = chatProperties.chatFrequencyPenalty.value,
                )
                val client = OpenAiClient(OpenAiSettings(appProperties.apiKey))
                client.requestChatWithStreaming(request).correctStreamResponse()

                val latestMessages = createMessages()
                session.messages = latestMessages
                logger.logRequest(request)
                logger.logMessages(latestMessages)
                launch {
                    updateChatSessionTitle(latestMessages, session)
                }
                // レスポンスが終わったら、次の入力用のメッセージ追加
                val lastFunctionCall = latestMessages.last().functionCall
                if (lastFunctionCall != null) {
                    addMessage(OpenAiChatRoleTypes.FUNCTION, lastFunctionCall.name)
                } else {
                    addMessage(OpenAiChatRoleTypes.USER)
                }
            }.onFailure {
                errorMessage.value = it.message ?: it.toString()
                logger.logError(it)
            }.also {
                logger.close()
                requesting.value = false
            }
        }
    }

    fun restoreOldSession(session: ChatHistory.ChatHistorySession) {
        val lastSession = currentChatSession
        launch {
            chatHistory.saveSession(lastSession)
        }
        currentChatSession = session
        messages.value = session.messages
    }

    private suspend fun Flow<OpenAiChatStreamResponse>.correctStreamResponse() {
        var firstTime = true
        var itemIndex = 0
        this.collect { streamResponse ->
            if (firstTime) {
                firstTime = false
                itemIndex = addMessage(OpenAiChatRoleTypes.ASSISTANT)
            }

            streamResponse.choices.firstOrNull()?.delta?.let { contentDelta ->
                val oldMessage = messages.value[itemIndex]
                val newMessage = oldMessage.add(contentDelta)
                updateMessage(itemIndex, newMessage)
            }
        }
    }

    private fun OpenAiChatMessage.add(delta: OpenAiChatStreamDelta): OpenAiChatMessage {
        val newRole = delta.role ?: this.role
        val newContent = delta.content?.let {
            this.content?.let {
                it + delta.content
            } ?: delta.content
        } ?: this.content
        val newFunction = this.functionCall.add(delta.functionCall)
        return OpenAiChatMessage(newRole, newContent, newFunction)
    }

    private fun OpenAiChatFunctionCallMessage?.add(
        delta: OpenAiChatFunctionCallMessageDelta?,
    ): OpenAiChatFunctionCallMessage? {
        if (this == null && delta == null) {
            return null
        }
        val newName = delta?.name ?: this?.name
        val newArguments = delta?.arguments?.let { deltaArguments ->
            this?.arguments?.let { thisArguments ->
                thisArguments + deltaArguments
            } ?: deltaArguments
        } ?: this?.arguments
        return OpenAiChatFunctionCallMessage(newName.orEmpty(), newArguments.orEmpty())
    }

    private fun createMessages(): List<OpenAiChatMessage> {
        val messageList = mutableListOf<OpenAiChatMessage>()
        val system = chatProperties.chatSystemPrompt.value
        if (system.isNotEmpty()) {
            messageList.add(OpenAiChatMessage(OpenAiChatRoleTypes.SYSTEM, system, null, null))
        }
        messageList.addAll(messages.value)
        return messageList
    }

    private fun OpenAiChatMessage.toggleRole(): OpenAiChatMessage {
        return when (this.role) {
            OpenAiChatRoleTypes.ASSISTANT -> OpenAiChatMessage(
                OpenAiChatRoleTypes.FUNCTION,
                this.content,
                this.functionCall,
                this.name,
            )

            OpenAiChatRoleTypes.USER -> OpenAiChatMessage(
                OpenAiChatRoleTypes.ASSISTANT,
                this.content,
                this.functionCall,
                this.name,
            )

            OpenAiChatRoleTypes.FUNCTION -> OpenAiChatMessage(
                OpenAiChatRoleTypes.USER,
                this.content,
                this.functionCall,
                this.name,
            )

            else -> error("should not reach here")
        }
    }

    override fun close() {
        runBlocking {
            job.cancelAndJoin()
            chatHistory.saveSession(currentChatSession)
        }
    }
}
