package com.doctorate.ui.network.retrofit

import com.doctorate.ui.entity.Char
import retrofit2.http.GET
import retrofit2.http.Header
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
    suspend fun syncCharacter(@Header("adminKey") adminKey: String, @Query("uid") uid: String): MutableMap<String, Char>
}