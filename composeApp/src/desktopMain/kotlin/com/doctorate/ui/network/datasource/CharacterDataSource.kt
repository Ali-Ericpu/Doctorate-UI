package com.doctorate.ui.network.datasource

import com.doctorate.ui.config.readConfig
import com.doctorate.ui.entity.Result
import com.doctorate.ui.entity.SaveCharBody
import com.doctorate.ui.network.client.NetworkModule
import com.doctorate.ui.network.retrofit.CharacterApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

/**
 * ClassName: CharacterDataSourse
 * Package: com.doctorate.ui.network.datasourse
 * Description:
 * @author Raincc
 * @Create 2024/8/14 20:09
 * @Version 1.0
 */
object CharacterDataSource {

    private var service = initService()

    private fun getServerUri(): String {
        val config = readConfig()
        return if (config.serverPort.isBlank()) config.serverUri else "${config.serverUri}:${config.serverPort}"
    }

    private fun initService() = Retrofit.Builder()
        .baseUrl(getServerUri().also { println(it) })
        .client(NetworkModule.providesOkHttpClient())
        .addConverterFactory(NetworkModule.providesNetworkJson().asConverterFactory("application/json".toMediaType()))
        .build()
        .create(CharacterApiService::class.java)

    fun reset() = CoroutineScope(Dispatchers.IO).launch {
        delay(200)
        service = initService()
    }

    suspend fun syncCharacter(adminKey: String, uid: String): Result {
        return service.syncCharacter(adminKey, uid)
    }

    suspend fun saveCharacter(adminKey: String, saveCharBody: SaveCharBody): Result {
        return service.saveCharacter(adminKey, saveCharBody)
    }

}