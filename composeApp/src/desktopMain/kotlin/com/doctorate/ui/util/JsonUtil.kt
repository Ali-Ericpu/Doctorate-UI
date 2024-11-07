package com.doctorate.ui.util

import com.fasterxml.jackson.core.util.DefaultIndenter
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
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

    val mapper = jacksonObjectMapper()
    private val factory = Thread.ofVirtual().factory()

    init {
        mapper.setDefaultPrettyPrinter(object : DefaultPrettyPrinter() {
            override fun createInstance(): DefaultPrettyPrinter? {
                this._arrayIndenter = DefaultIndenter()
                this._objectFieldValueSeparatorWithSpaces = _separators.objectFieldValueSeparator + " "
                this._arrayEmptySeparator = ""
                this._objectEmptySeparator = ""
                return this
            }
        })
    }

    fun transToMap(file: File): Map<String, Any> {
        return mapper.readValue<Map<String, Any>>(file)
    }

    fun transToMap(json: String): Map<String, Any> {
        return mapper.readValue<Map<String, Any>>(json)
    }

    inline fun <reified T> fromJson(file: File): T = mapper.readValue<T>(file)

    inline fun <reified T> fromJson(json: String): T = mapper.readValue<T>(json)

    inline fun <reified T> fromMap(map: Map<String, Any>): T = mapper.convertValue<T>(map)

    fun toJson(any: Any): String = mapper.writeValueAsString(any)

    fun toPrettyJson(any: Any): String = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(any)

    fun writeToFile(any: Any, file: File) = writeToFile(toPrettyJson(any), file)

    fun writeToFile(json: String, file: File) {
        factory.newThread { file.parentFile.mkdirs().also { file.writeText(json) } }.start()
        log().info("Write Json to file: {}", file.absolutePath)
    }

}