package com.doctorate.ui.network.client

import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

/**
 * ClassName: NetworkModule
 * Package: com.doctorate.ui.network.di
 * Description:
 * @author Raincc
 * @Create 2024/8/14 20:11
 * @Version 1.0
 */
object NetworkModule {

    fun providesNetworkJson(): Json = Json {
        ignoreUnknownKeys = true
    }

    fun okhttpCallFactory(okHttpClient: OkHttpClient): Call.Factory = okHttpClient

    fun providesOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .build()
    }

}