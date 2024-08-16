package com.doctorate.ui.network.datasource

import com.doctorate.ui.config.readConfig
import com.doctorate.ui.entity.Char
import com.doctorate.ui.network.client.NetworkModule
import com.doctorate.ui.network.retrofit.CharacterApiService
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

    private val config = readConfig()
    private val serverUri = "http://${config.serverUri}:${config.serverPort}"

    private val service = Retrofit.Builder()
        .baseUrl(serverUri)
        .client(NetworkModule.providesOkHttpClient())
        .addConverterFactory(NetworkModule.providesNetworkJson())
        .build()
        .create<CharacterApiService>(CharacterApiService::class.java)

    suspend fun syncCharacter(adminKey: String, uid: String): MutableMap<String, Char> {
        return service.syncCharacter(adminKey, uid)
    }


}