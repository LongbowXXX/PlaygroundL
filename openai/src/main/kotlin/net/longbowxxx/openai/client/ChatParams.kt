/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.openai.client

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

const val CHAT_TEMPERATURE_DEFAULT = 1.0f
const val CHAT_TOP_P_DEFAULT = 1.0f
const val CHAT_MAX_TOKENS_DEFAULT = 512
const val CHAT_PRESENCE_PENALTY_DEFAULT = 0.0f
const val CHAT_FREQUENCY_PENALTY_DEFAULT = 0.0f

@Serializable
data class OpenAiChatRequest(
    val model: String,
    val messages: List<OpenAiChatMessage>,
    val functions: List<OpenAiChatFunction>? = null,
    @SerialName("function_call")
    val functionCall: JsonElement? = null,
    val temperature: Float = CHAT_TEMPERATURE_DEFAULT,
    @SerialName("top_p")
    val topP: Float = CHAT_TOP_P_DEFAULT,
    val n: Int = 1,
    val stream: Boolean = false,
    val stop: List<String>? = null,
    @SerialName("max_tokens")
    val maxTokens: Int = CHAT_MAX_TOKENS_DEFAULT,
    @SerialName("presence_penalty")
    val presencePenalty: Float = CHAT_PRESENCE_PENALTY_DEFAULT,
    @SerialName("frequency_penalty")
    val frequencyPenalty: Float = CHAT_FREQUENCY_PENALTY_DEFAULT,
    @SerialName("logic_bias")
    val logicBias: Map<Int, Int>? = null,
    val user: String? = null,
)

const val OPENAI_CHAT_MODEL_GPT_35_TURBO = "gpt-3.5-turbo"
const val OPENAI_CHAT_MODEL_GPT_4 = "gpt-4"
const val OPENAI_CHAT_MODEL_GPT_35_TURBO_0613 = "gpt-3.5-turbo-0613"
const val OPENAI_CHAT_MODEL_GPT_4_0613 = "gpt-4-0613"

fun ofFunctionCallNone() = JsonPrimitive("none")
fun ofFunctionCallAuto() = JsonPrimitive("auto")
fun ofFunctionCallForce(name: String) = JsonObject(mapOf("name" to JsonPrimitive(name)))

@Serializable
data class OpenAiChatFunction(
    val name: String,
    val description: String? = null,
    val parameters: OpenAiChatParameter,
)

@Serializable
sealed class OpenAiChatParameter {
    @Serializable
    @SerialName("object")
    data class OpenAiChatParameterObject(
        val properties: Map<String, OpenAiChatProperty>,
        val required: List<String>,
    ) : OpenAiChatParameter()
}

@Serializable
data class OpenAiChatProperty(
    val type: String,
    val description: String? = null,
    val enum: List<String>? = null,
)

@Serializable
data class OpenAiChatMessage(
    val role: OpenAiChatRoleTypes,
    val content: String? = null,
    @SerialName("function_call")
    val functionCall: OpenAiChatFunctionCallMessage? = null,
    val name: String? = null,
)

@Serializable
data class OpenAiChatFunctionCallMessage(
    val name: String,
    val arguments: String,
)

@Serializable
enum class OpenAiChatRoleTypes {
    @SerialName("system")
    SYSTEM,

    @SerialName("assistant")
    ASSISTANT,

    @SerialName("user")
    USER,

    @SerialName("function")
    FUNCTION,
}

@Serializable
data class OpenAiChatStreamResponse(
    val id: String,
    val `object`: String,
    val created: Int,
    val model: String,
    val choices: List<OpenAiChatStreamChoice>,
)

@Serializable
data class OpenAiChatStreamChoice(
    val delta: OpenAiChatStreamDelta,
    val index: Int,
    @SerialName("finish_reason")
    val finishReason: String? = null,
)

@Serializable
data class OpenAiChatStreamDelta(
    val role: OpenAiChatRoleTypes? = null,
    val content: String? = null,
    @SerialName("function_call")
    val functionCall: OpenAiChatFunctionCallMessageDelta? = null,
)

@Serializable
data class OpenAiChatFunctionCallMessageDelta(
    val name: String? = null,
    val arguments: String? = null,
)
