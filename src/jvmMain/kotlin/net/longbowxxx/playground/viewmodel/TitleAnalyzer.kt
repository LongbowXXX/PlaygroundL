/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.viewmodel

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import net.longbowxxx.generativeai.DiscussMessage
import net.longbowxxx.openai.client.OPENAI_CHAT_MODEL_GPT_35_TURBO_0613
import net.longbowxxx.openai.client.OpenAiChatMessage
import net.longbowxxx.openai.client.OpenAiChatRequest
import net.longbowxxx.openai.client.OpenAiChatRoleTypes
import net.longbowxxx.openai.client.OpenAiClient
import net.longbowxxx.openai.client.OpenAiSettings
import net.longbowxxx.playground.history.ChatHistory
import net.longbowxxx.playground.history.DiscussHistory
import net.longbowxxx.playground.utils.DebugLogLevel
import net.longbowxxx.playground.utils.log

suspend fun updateChatSessionTitle(messages: List<OpenAiChatMessage>, session: ChatHistory.ChatHistorySession) {
    var responseString = ""
    runCatching {
        val requestMessages = mutableListOf<OpenAiChatMessage>().apply {
            // 解析するときに古いSystemプロンプトが邪魔をするのでそれ以外を採用
            addAll(messages.filter { it.role != OpenAiChatRoleTypes.SYSTEM })
            add(
                OpenAiChatMessage(
                    OpenAiChatRoleTypes.SYSTEM,
                    """
                        ## 命令:
                        今までの user と assistant の会話を解析し、タイトルとカテゴリ分類を作成してください。  
                        出力フォーマットを必ず守ってください。例外はありません。
                        
                        ## 出力フォーマット:
                        {
                          "title":"タイトル",
                          "categories":[
                            "カテゴリー1",
                            "カテゴリー2"
                          ],
                          "reason":"この結果になった理由"
                        }
                        
                        ## 出力例:
                        {
                          "title":"パスワード入力について",
                          "categories":[
                            "kotlin",
                            "compose-desktop",
                            "TextField"
                          ],
                          "reason":"パスワード入力について質問されているため"
                        }
                    """.trimIndent(),
                    null,
                    null,
                ),
            )
        }
        val request = OpenAiChatRequest(
            OPENAI_CHAT_MODEL_GPT_35_TURBO_0613,
            messages = requestMessages,
            stream = true,
            temperature = 0f,
        )
        val client = OpenAiClient(OpenAiSettings(appProperties.apiKey))
        client.requestChatWithStreaming(request).collect { streamResponse ->
            streamResponse.choices.firstOrNull()?.delta?.content?.let { contentDelta ->
                responseString += contentDelta
            }
        }
        val summary = decodeJson.decodeFromString<ChatSessionSummary>(responseString)
        session.title = summary.title
        session.categories = summary.categories
    }.onFailure {
        log(DebugLogLevel.WARN, "TitleAnalyzer", it) {
            "updateChatSessionTitle failed. responseString=$responseString"
        }
    }.also {
        chatHistory.saveSession(session)
    }
}

suspend fun updateDiscussSessionTitle(messages: List<DiscussMessage>, session: DiscussHistory.DiscussHistorySession) {
    var responseString = ""
    runCatching {
        // 2023/06/17 Palm2 ではうまく解析できなかったので、OpenAIで解析
        val requestMessages = mutableListOf<OpenAiChatMessage>().apply {
            addAll(messages.map { OpenAiChatMessage(it.author.toRoleType(), it.content) })
            add(
                OpenAiChatMessage(
                    OpenAiChatRoleTypes.SYSTEM,
                    """
                        ## 命令:
                        今までの user と assistant の会話を解析し、タイトルとカテゴリ分類を作成してください。  
                        出力フォーマットを必ず守ってください。例外はありません。
                        
                        ## 出力フォーマット:
                        {
                          "title":"タイトル",
                          "categories":[
                            "カテゴリー1",
                            "カテゴリー2"
                          ],
                          "reason":"この結果になった理由"
                        }
                        
                        ## 出力例:
                        {
                          "title":"パスワード入力について",
                          "categories":[
                            "kotlin",
                            "compose-desktop",
                            "TextField"
                          ],
                          "reason":"パスワード入力について質問されているため"
                        }
                    """.trimIndent(),
                    null,
                    null,
                ),
            )
        }
        val request = OpenAiChatRequest(
            OPENAI_CHAT_MODEL_GPT_35_TURBO_0613,
            messages = requestMessages,
            stream = true,
            temperature = 0f,
        )
        val client = OpenAiClient(OpenAiSettings(appProperties.apiKey))
        client.requestChatWithStreaming(request).collect { streamResponse ->
            streamResponse.choices.firstOrNull()?.delta?.content?.let { contentDelta ->
                responseString += contentDelta
            }
        }
        val summary = decodeJson.decodeFromString<ChatSessionSummary>(responseString)
        session.title = summary.title
        session.categories = summary.categories
    }.onFailure {
        log(DebugLogLevel.WARN, "TitleAnalyzer", it) {
            "updateDiscussSessionTitle failed. responseString=$responseString"
        }
    }.also {
        discussHistory.saveSession(session)
    }
}

private fun String.toRoleType() = when (this) {
    "0" -> OpenAiChatRoleTypes.USER
    "1" -> OpenAiChatRoleTypes.ASSISTANT
    else -> error("Unknown Author $this")
}

private val decodeJson = Json {
    encodeDefaults = false
    ignoreUnknownKeys = true
}

@Serializable
data class ChatSessionSummary(
    val title: String,
    val categories: List<String>,
)
