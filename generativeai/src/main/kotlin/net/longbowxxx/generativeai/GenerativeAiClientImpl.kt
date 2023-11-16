/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.generativeai

import com.google.ai.generativelanguage.v1beta3.DiscussServiceClient
import com.google.ai.generativelanguage.v1beta3.DiscussServiceSettings
import com.google.ai.generativelanguage.v1beta3.Example
import com.google.ai.generativelanguage.v1beta3.GenerateMessageRequest
import com.google.ai.generativelanguage.v1beta3.Message
import com.google.ai.generativelanguage.v1beta3.MessagePrompt
import com.google.api.gax.core.FixedCredentialsProvider
import com.google.api.gax.grpc.InstantiatingGrpcChannelProvider
import com.google.api.gax.rpc.FixedHeaderProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GenerativeAiClientImpl(settings: GenerativeAiSettings) : GenerativeAiClient {
    private val headers =
        mapOf(
            "x-goog-api-key" to settings.apiKey,
        )

    override suspend fun requestDiscuss(discussRequest: DiscussRequest): DiscussResponse {
        return withContext(Dispatchers.IO) {
            logGenerativeAiRequest { "$discussRequest" }
            val provider =
                InstantiatingGrpcChannelProvider.newBuilder().apply {
                    setHeaderProvider(FixedHeaderProvider.create(headers))
                }.build()

            val settings =
                DiscussServiceSettings.newBuilder().apply {
                    transportChannelProvider = provider
                    credentialsProvider = FixedCredentialsProvider.create(null)
                }.build()

            val examples =
                discussRequest.prompt.examples.map { example ->
                    Example.newBuilder().apply {
                        input = example.input.toMessage()
                        output = example.output.toMessage()
                    }.build()
                }

            val messages =
                discussRequest.prompt.messages.map {
                    it.toMessage()
                }

            val messagePrompt: MessagePrompt =
                MessagePrompt.newBuilder().apply {
                    addAllMessages(messages)
                    context = discussRequest.prompt.context
                    addAllExamples(examples)
                }.build()

            val request =
                GenerateMessageRequest.newBuilder().apply {
                    model = discussRequest.model
                    prompt = messagePrompt
                    temperature = discussRequest.temperature
                    candidateCount = discussRequest.candidateCount
                }.build()

            DiscussServiceClient.create(settings).use { client ->
                val messageResponse = client.generateMessage(request)

                messageResponse.candidatesList.map { message ->
                    DiscussMessage(message.author, message.content)
                }.let {
                    DiscussResponse(it)
                }.also {
                    logGenerativeAiResponse { "$it" }
                }
            }
        }
    }

    private fun DiscussMessage.toMessage(): Message {
        return Message.newBuilder().apply {
            author = this@toMessage.author
            content = this@toMessage.content
        }.build()
    }
}
