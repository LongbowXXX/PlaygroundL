package net.longbowxxx.playground.history

import kotlinx.coroutines.runBlocking
import net.longbowxxx.openai.client.OpenAiChatMessage
import net.longbowxxx.openai.client.OpenAiChatRoleTypes
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ChatHistoryTest {

    @Test
    fun basicFlow() = runBlocking {
        ChatHistory("build/tmp/db", "testFile.realm").use { history ->
            history.clearHistory()
            history.saveSession(
                ChatHistory.ChatHistorySession(
                    "title",
                    listOf("category1"),
                    listOf(
                        OpenAiChatMessage(
                            OpenAiChatRoleTypes.ASSISTANT,
                            "content",
                        ),
                    ),
                ),
            )

            val items = history.getHistory()
            assertEquals(items.size, 1)
            history.saveSession(items.first())
            history.removeHistory(items.first())
            val items2 = history.getHistory()
            assertEquals(items2.size, 0)
        }
    }
}
