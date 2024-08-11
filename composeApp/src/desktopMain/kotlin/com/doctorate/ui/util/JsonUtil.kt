package com.doctorate.ui.util

import com.google.gson.GsonBuilder
import java.io.File

/**
 * ClassName: JsonUtil
 * Package: com.doctorate.ui.util
 * Description:
 * @author Raincc
 * @Create 2024/8/10 23:54
 * @Version 1.0
 */
object JsonUtil {

    private val gson = GsonBuilder().setPrettyPrinting().serializeNulls().create()
    private val factory = Thread.ofVirtual().factory()

    fun toJson(obj: Any): String {
        return gson.toJson(obj)
    }

    fun <T> fromJson(file: File, clazz: Class<T>): T {
        val readText = file.readText(Charsets.UTF_8)
        return gson.fromJson(readText, clazz)
    }

    fun <T> fromJson(json: String, clazz: Class<T>): T {
        return gson.fromJson(json, clazz)
    }

    fun fromJson(json: String): Map<String, Any> {
        return gson.fromJson(json, Map::class.java) as Map<String, Any>
    }

    fun writeJson(json: String, file: File) {
        factory.newThread{ file.parentFile.mkdirs().also { file.writeText(json) } }.start()
    }

    fun writeJson(any: Any, file: File) {
        writeJson(gson.toJson(any), file)
    }

}