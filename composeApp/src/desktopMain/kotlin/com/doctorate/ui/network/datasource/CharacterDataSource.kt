package com.doctorate.ui.network.datasource

import com.doctorate.ui.config.readConfig
import com.doctorate.ui.entity.Char
import com.doctorate.ui.entity.Result
import com.doctorate.ui.network.client.NetworkModule
import com.doctorate.ui.network.retrofit.CharacterApiService
import com.doctorate.ui.req.GainItemReq
import com.doctorate.ui.req.SaveCharReq
import com.doctorate.ui.req.UnlockAllCharReq
import com.doctorate.ui.util.JsonUtil
import com.doctorate.ui.util.log
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
        .baseUrl(getServerUri().also { log().info("Server URI: {}", it) })
        .client(NetworkModule.providesOkHttpClient())
        .addConverterFactory(NetworkModule.providesNetworkJson().asConverterFactory("application/json".toMediaType()))
        .build()
        .create(CharacterApiService::class.java)

    fun reset() = CoroutineScope(Dispatchers.IO).launch {
        delay(200)
        service = initService()
    }

    suspend fun syncCharacter(adminKey: String, uid: String): Result<Map<String, Char>> {
        log().info("syncCharacter UID: {}", uid)
        return service.syncCharacter(adminKey, uid)
    }

    suspend fun saveCharacter(adminKey: String, uid: String, saveCharBody: SaveCharReq): Result<String?> {
        log().info("saveCharacter UID: {} -> {}", uid, saveCharBody)
        return service.saveCharacter(adminKey, uid, saveCharBody)
    }

    suspend fun gainItem(adminKey: String, uid: String, gainItemReq: GainItemReq): Result<String?> {
        log().info("gainItem UID: {} -> {}",uid, JsonUtil.toJson(gainItemReq))
        return service.gainItem(adminKey, uid, gainItemReq)
    }

    suspend fun unlockAllStages(adminKey: String, uid: String): Result<String?> {
        log().info("unlockAllStages UID: {}", uid)
        return service.unlockAllStage(adminKey, uid)
    }

    suspend fun unlockAllFlags(adminKey: String, uid: String): Result<String?> {
        log().info("unlockAllFlags UID: {}", uid)
        return service.unlockAllStage(adminKey, uid)
    }

    suspend fun unlockAllChar(adminKey: String, uid: String, unlockAllCharReq: UnlockAllCharReq): Result<String?> {
        log().info("unlockAllChar UID: {} -> {}", uid, JsonUtil.toJson(unlockAllCharReq))
        return service.unlockAllChar(adminKey, uid, unlockAllCharReq)
    }

}