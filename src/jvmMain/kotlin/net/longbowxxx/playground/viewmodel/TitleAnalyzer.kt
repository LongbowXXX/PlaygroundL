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
import net.longbowxxx.openai.client.OPENAI_CHAT_MODEL_GPT_35_TURBO
import net.longbowxxx.openai.client.OPENAI_CHAT_URL
import net.longbowxxx.openai.client.OpenAiChatMessage
import net.longbowxxx.openai.client.OpenAiChatRequest
import net.longbowxxx.openai.client.OpenAiChatRoleTypes
import net.longbowxxx.openai.client.OpenAiClient
import net.longbowxxx.openai.client.OpenAiSettings
import net.longbowxxx.playground.history.ChatHistory

suspend fun updateChatSessionTitle(messages: List<OpenAiChatMessage>, session: ChatHistory.ChatHistorySession) {
    runCatching {
        val requestMessages = mutableListOf<OpenAiChatMessage>().apply {
            // 解析するときに古いSystemプロンプトが邪魔をするのでそれ以外を採用
            addAll(messages.filter { it.role != OpenAiChatRoleTypes.SYSTEM })
            add(
                OpenAiChatMessage(
                    OpenAiChatRoleTypes.SYSTEM,
                    """
                        会話を解析し、タイトルとカテゴリ分類を作成してください。  
                        今ある情報だけで出力を作成してください。例外はありません。
                        
                        ## 出力フォーマット
                        {
                          "title":"タイトル",
                          "categories":[
                            "カテゴリー1",
                            "カテゴリー2"
                          ],
                          "reason":"この結果になった理由"
                        }
                        
                        ## 出力例
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
                ),
            )
        }
        val request = OpenAiChatRequest(
            OPENAI_CHAT_MODEL_GPT_35_TURBO,
            messages = requestMessages,
            stream = true,
            temperature = 0f,
        )
        val client = OpenAiClient(OpenAiSettings(OPENAI_CHAT_URL, appProperties.apiKey))
        var responseString = ""
        client.requestChatWithStreaming(request).collect { streamResponse ->
            streamResponse.choices.firstOrNull()?.delta?.content?.let { contentDelta ->
                responseString += contentDelta
            }
        }
        val summary = decodeJson.decodeFromString<ChatSessionSummary>(responseString)
        session.title = summary.title
        session.categories = summary.categories
    }.also {
        chatHistory.saveSession(session)
    }
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
