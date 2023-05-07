package net.longbowxxx.playground.history

import kotlinx.coroutines.runBlocking
import net.longbowxxx.openai.client.OpenAiChatMessage
import net.longbowxxx.openai.client.OpenAiChatRoleTypes
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ChatHistoryTest {

    @Test
    fun basicFlow() = runBlocking {
        ChatHistory().use { history ->
            history.clearHistory()
            history.addHistory(
                ChatHistory.ChatHistoryItem(
                    "title",
                    listOf("category1"),
                    listOf(
                        OpenAiChatMessage(
                            OpenAiChatRoleTypes.USER,
                            "content",
                        ),
                    ),
                ),
            )

            val items = history.getHistory()
            assertEquals(items.size, 1)
            history.updateHistory(items.first())
            history.removeHistory(items.first())
            val items2 = history.getHistory()
            assertEquals(items2.size, 0)
        }
    }
}
