/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.viewmodel

import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.CancellationException
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
import net.longbowxxx.openai.client.updateContent
import net.longbowxxx.playground.function.ChatFunctionLoader
import net.longbowxxx.playground.function.ChatFunctionPlugin
import net.longbowxxx.playground.history.ChatHistory
import net.longbowxxx.playground.logger.ChatLogger
import net.longbowxxx.playground.utils.appDataDirectory
import java.io.Closeable
import java.io.File
import kotlin.coroutines.CoroutineContext

class ChatViewModel(dispatcher: CoroutineDispatcher = Dispatchers.Default) : CoroutineScope, Closeable {
    private val job = Job()
    override val coroutineContext: CoroutineContext = dispatcher + job

    companion object {
        private val INITIAL_MESSAGES = listOf(OpenAiChatMessage(OpenAiChatRoleTypes.USER, "", null, null))
    }

    val messages = mutableStateOf(INITIAL_MESSAGES)
    val errorMessage = mutableStateOf("")
    val requesting = mutableStateOf(false)
    val history = mutableStateOf<List<ChatHistory.ChatHistorySession>>(emptyList())
    val models =
        listOf(
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

    private val functionLoader = ChatFunctionLoader()
    val allFunctions = mutableStateOf(functionLoader.loadPlugins(File("chatFunction")).map { it to false })
    private val activeFunctions: List<ChatFunctionPlugin>
        get() {
            return allFunctions.value.filter { it.second }.map { it.first }
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

    fun updateFunctionEnabled(
        index: Int,
        enabled: Boolean,
    ) {
        val newList = allFunctions.value.toMutableList()
        newList[index] = newList[index].first to enabled
        allFunctions.value = newList.toList()
    }

    private fun updateMessage(
        index: Int,
        message: OpenAiChatMessage,
    ) {
        val newList = mutableListOf<OpenAiChatMessage>()
        newList.addAll(messages.value)
        require(index < newList.size)
        newList[index] = message
        messages.value = newList
    }

    fun updateMessageContent(
        index: Int,
        content: String,
    ) {
        val newList = mutableListOf<OpenAiChatMessage>()
        newList.addAll(messages.value)
        require(index < newList.size)
        newList[index] = newList[index].updateContent(content)
        messages.value = newList
    }

    fun removeMessage(index: Int) {
        val newList = mutableListOf<OpenAiChatMessage>()
        newList.addAll(messages.value)
        require(index < newList.size)
        newList.removeAt(index)
        messages.value = newList
        currentChatSession.messages = newList
    }

    fun addMessage(
        role: OpenAiChatRoleTypes,
        content: String? = null,
        name: String? = null,
    ): Int {
        val newList = mutableListOf<OpenAiChatMessage>()
        newList.addAll(messages.value)
        newList.add(OpenAiChatMessage(role, content, null, name))
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

    fun cancelRequestChat() {
        currentRequestJob?.cancel()
    }

    fun requestChat() {
        val lastJob = currentRequestJob
        lastJob?.cancel()
        currentRequestJob =
            launch {
                lastJob?.join()
                // 古いエラーを消す
                errorMessage.value = ""

                val currentModel = chatProperties.chatModel.value
                val functionEnabled = currentModel.isFunctionAvailable()

                requesting.value = true
                val session = currentChatSession
                val logger = ChatLogger(appDataDirectory)
                val plugins =
                    if (functionEnabled) {
                        activeFunctions
                    } else {
                        null
                    }
                runCatching {
                    requestInternal(currentModel, plugins, logger)
                    logger.logMessages(session.messages)

                    launch {
                        updateChatSessionTitle(session.messages, session)
                    }
                    Unit
                }.onFailure {
                    if (it !is CancellationException) {
                        errorMessage.value = it.message ?: it.toString()
                        logger.logError(it)
                    }
                }.also {
                    logger.close()
                    requesting.value = false
                }
            }
    }

    private suspend fun requestInternal(
        currentModel: String,
        plugins: List<ChatFunctionPlugin>?,
        logger: ChatLogger,
    ) {
        val functions =
            plugins?.map { it.functionSpec }.let {
                // function がない場合、Requests の functions は null でないとエラーになる
                if (it.isNullOrEmpty()) {
                    null
                } else {
                    it
                }
            }
        val historyMessages = createMessages()
        val request =
            OpenAiChatRequest(
                currentModel,
                messages = historyMessages.filter { it.hasContent },
                functions = functions,
                stream = true,
                temperature = chatProperties.chatTemperature.value,
                topP = chatProperties.chatTopP.value,
                maxTokens = chatProperties.chatMaxTokens.value,
                presencePenalty = chatProperties.chatPresencePenalty.value,
                frequencyPenalty = chatProperties.chatFrequencyPenalty.value,
            )
        logger.logRequest(request)
        val client = OpenAiClient(OpenAiSettings(appProperties.apiKey))
        val newMessage = client.requestChatWithStreaming(request).correctStreamResponse()
        // レスポンスが終わったら、次の入力用のメッセージ追加
        val lastFunctionCall = newMessage.functionCall
        if (lastFunctionCall != null) {
            // function 呼び出し実行
            val functionResponse = callFunction(lastFunctionCall)
            // function 結果をメッセージに追加
            addMessage(
                OpenAiChatRoleTypes.FUNCTION,
                functionResponse,
                lastFunctionCall.name,
            )
            // 次の request を行う
            requestInternal(currentModel, plugins, logger)
        } else {
            addMessage(OpenAiChatRoleTypes.USER)
        }
    }

    private suspend fun callFunction(functionCallMessage: OpenAiChatFunctionCallMessage): String {
        return activeFunctions.firstOrNull { plugin ->
            plugin.functionSpec.name == functionCallMessage.name
        }?.execute(functionCallMessage.arguments) ?: error("not found function : $functionCallMessage")
    }

    fun restoreOldSession(session: ChatHistory.ChatHistorySession) {
        val lastSession = currentChatSession
        launch {
            chatHistory.saveSession(lastSession)
        }
        currentChatSession = session
        messages.value = session.messages
    }

    private suspend fun Flow<OpenAiChatStreamResponse>.correctStreamResponse(): OpenAiChatMessage {
        var firstTime = true
        var itemIndex = 0
        var completedMessage: OpenAiChatMessage? = null
        this.collect { streamResponse ->
            if (firstTime) {
                firstTime = false
                itemIndex = addMessage(OpenAiChatRoleTypes.ASSISTANT)
            }

            streamResponse.choices.firstOrNull()?.delta?.let { contentDelta ->
                val oldMessage = messages.value[itemIndex]
                val newMessage = oldMessage.add(contentDelta)
                completedMessage = newMessage
                updateMessage(itemIndex, newMessage)
            }
        }
        return checkNotNull(completedMessage) { "correctStreamResponse() : completedMessage must not be null." }
    }

    private fun OpenAiChatMessage.add(delta: OpenAiChatStreamDelta): OpenAiChatMessage {
        val newRole = delta.role ?: this.role
        val newContent =
            delta.content?.let {
                this.content?.let {
                    it + delta.content
                } ?: delta.content
            } ?: this.content
        val newFunction = this.functionCall.add(delta.functionCall)
        return OpenAiChatMessage(newRole, newContent, newFunction)
    }

    private fun OpenAiChatFunctionCallMessage?.add(delta: OpenAiChatFunctionCallMessageDelta?): OpenAiChatFunctionCallMessage? {
        if (this == null && delta == null) {
            return null
        }
        val newName = delta?.name ?: this?.name
        val newArguments =
            delta?.arguments?.let { deltaArguments ->
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

    override fun close() {
        runBlocking {
            job.cancelAndJoin()
            chatHistory.saveSession(currentChatSession)
        }
    }
}
