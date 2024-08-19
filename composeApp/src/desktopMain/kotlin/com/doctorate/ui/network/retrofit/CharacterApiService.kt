package com.doctorate.ui.network.retrofit

import com.doctorate.ui.entity.Result
import com.doctorate.ui.entity.SaveCharBody
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
    suspend fun syncCharacter(@Header("adminKey") adminKey: String, @Query("uid") uid: String): Result

    @POST("admin/character/save")
    suspend fun saveCharacter(@Header("adminKey") adminKey: String, @Body body: SaveCharBody): Result

}