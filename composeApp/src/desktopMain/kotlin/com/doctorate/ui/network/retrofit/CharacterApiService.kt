package com.doctorate.ui.network.retrofit

import com.doctorate.ui.entity.Char
import com.doctorate.ui.entity.Result
import com.doctorate.ui.req.GainItemReq
import com.doctorate.ui.req.SaveCharReq
import com.doctorate.ui.req.UnlockAllCharReq
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * ClassName: CharacterService
 * Package: com.doctorate.ui.network.retrofit
 * Description:
 * @author Raincc
 * @Create 2024/8/14 19:55
 * @Version 1.0
 */
interface CharacterApiService {

    @GET("admin/character/sync")
    suspend fun syncCharacter(
        @Header("adminKey") adminKey: String,
        @Query("uid") uid: String
    ): Result<Map<String, Char>>

    @POST("admin/character/save")
    suspend fun saveCharacter(
        @Header("adminKey") adminKey: String,
        @Header("uid") uid: String,
        @Body body: SaveCharReq
    ): Result<String?>

    @POST("admin/gainItem")
    suspend fun gainItem(
        @Header("adminKey") adminKey: String,
        @Header("uid") uid: String,
        @Body body: GainItemReq
    ): Result<String?>

    @POST("admin/unlockAllStages")
    suspend fun unlockAllStage(@Header("adminKey") adminKey: String, @Header("uid") uid: String): Result<String?>

    @POST("admin/unlockAllFlags")
    suspend fun unlockAllFlag(@Header("adminKey") adminKey: String, @Header("uid") uid: String): Result<String?>

    @POST("admin/unlockAllChar")
    suspend fun unlockAllChar(
        @Header("adminKey") adminKey: String,
        @Header("uid") uid: String,
        @Body body: UnlockAllCharReq
    ): Result<String?>

}