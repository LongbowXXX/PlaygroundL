/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.generativeai

import com.google.ai.generativelanguage.v1beta2.DiscussServiceClient
import com.google.ai.generativelanguage.v1beta2.DiscussServiceSettings
import com.google.ai.generativelanguage.v1beta2.Example
import com.google.ai.generativelanguage.v1beta2.GenerateMessageRequest
import com.google.ai.generativelanguage.v1beta2.Message
import com.google.ai.generativelanguage.v1beta2.MessagePrompt
import com.google.api.gax.core.FixedCredentialsProvider
import com.google.api.gax.grpc.InstantiatingGrpcChannelProvider
import com.google.api.gax.rpc.FixedHeaderProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GenerativeAiClientImpl(private val settings: GenerativeAiSettings) : GenerativeAiClient {
    private val headers = mapOf(
        "x-goog-api-key" to settings.apiKey,
    )

    override suspend fun requestDiscuss() {
        withContext(Dispatchers.IO) {
            val provider = InstantiatingGrpcChannelProvider.newBuilder()
                .setHeaderProvider(FixedHeaderProvider.create(headers))
                .build()

            val settings = DiscussServiceSettings.newBuilder()
                .setTransportChannelProvider(provider)
                .setCredentialsProvider(FixedCredentialsProvider.create(null))
                .build()

            val input = Message.newBuilder()
                .setContent("What is the capital of California?")
                .build()

            val response = Message.newBuilder()
                .setContent("If the capital of California is what you seek, Sacramento is where you ought to peek.")
                .build()

            val californiaExample = Example.newBuilder()
                .setInput(input)
                .setOutput(response)
                .build()

            val palmMessage: Message = Message.newBuilder()
                .setAuthor("0")
                .setContent("How tall is the Eiffel Tower?")
                .build()

            val messagePrompt: MessagePrompt = MessagePrompt.newBuilder()
                .addMessages(palmMessage) // required
                .setContext("Respond to all questions with a rhyming poem.") // optional
                .addExamples(californiaExample) // use addAllExamples() to add a list of examples
                .build()

            val request = GenerateMessageRequest.newBuilder()
                .setModel("models/chat-bison-001") // Required, which model to use to generate the result
                .setPrompt(messagePrompt) // Required
                .setTemperature(0.5f) // Optional, controls the randomness of the output
                .setCandidateCount(1) // Optional, the number of generated messages to return
                .build()
            DiscussServiceClient.create(settings).use { client ->
                val messageResponse = client.generateMessage(request)

                val returnedMessage = messageResponse.candidatesList[0]

                println(returnedMessage)
            }
        }
    }
}
