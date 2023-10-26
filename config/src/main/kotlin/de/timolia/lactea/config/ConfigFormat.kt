package de.timolia.lactea.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import java.io.File
import java.io.IOException

abstract class ConfigFormat (
    val extension: String,
    val name: String,
) {
    val objectMapper: ObjectMapper by lazy(::createObjectMapper)

    protected abstract fun createObjectMapper(): ObjectMapper

    @Throws(IOException::class)
    fun <T> loadConfig(file: File, clazz: Class<T>): T {
        return objectMapper.readValue(file, clazz)
    }

    companion object {
        var YAML: ConfigFormat = object : ConfigFormat("yml", "yaml") {
            public override fun createObjectMapper(): ObjectMapper {
                val mapper = ObjectMapper(YAMLFactory())
                mapper.findAndRegisterModules()
                return mapper
            }
        }
        var JSON: ConfigFormat = object : ConfigFormat("json", "json") {
            public override fun createObjectMapper(): ObjectMapper {
                val mapper = ObjectMapper()
                mapper.findAndRegisterModules()
                return mapper
            }
        }
    }
}
