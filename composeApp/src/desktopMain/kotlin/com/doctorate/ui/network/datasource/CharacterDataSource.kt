package com.doctorate.ui.network.datasource

import com.doctorate.ui.config.readConfig
import com.doctorate.ui.entity.Result
import com.doctorate.ui.entity.SaveCharBody
import com.doctorate.ui.network.client.NetworkModule
import com.doctorate.ui.network.retrofit.CharacterApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
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

    private fun getServerUri(): String{
        val config = readConfig()
        return if (config.serverPort.isBlank()) config.serverUri else "${config.serverUri}:${config.serverPort}"
    }

    private val service = Retrofit.Builder()
        .baseUrl(getServerUri())
        .client(NetworkModule.providesOkHttpClient())
        .addConverterFactory(NetworkModule.providesNetworkJson().asConverterFactory("application/json".toMediaType()))
        .build()
        .create(CharacterApiService::class.java)

    suspend fun syncCharacter(adminKey: String, uid: String): Result {
        return service.syncCharacter(adminKey, uid)
    }

    suspend fun saveCharacter(adminKey: String, saveCharBody: SaveCharBody): Result {
        return service.saveCharacter(adminKey, saveCharBody)
    }

}