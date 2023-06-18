/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.function

import net.longbowxxx.openai.client.OpenAiChatFunction

class MockFunctionPlugin(override val functionSpec: OpenAiChatFunction) : ChatFunctionPlugin() {
    override suspend fun executeInternal(arguments: String, context: FunctionCallContext): String {
        // モックなので何もせずに空文字を返す
        return ""
    }
}
