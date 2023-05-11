/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.generativeai

abstract class GenerativeAiClientBase(val apiKey: String) {
    val headers = mapOf(
        "x-google-api-key" to apiKey
    )

    fun aaa() {

//        InstantiatingGrpcChannelProvider
//        InstantiatingGrpcChannelProvider provider = InstantiatingGrpcChannelProvider.newBuilder()
//            .setHeaderProvider(FixedHeaderProvider.create(headers))
//            .build();
    }
}
