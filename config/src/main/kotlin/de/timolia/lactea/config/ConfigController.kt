package de.timolia.lactea.config

import java.io.File
import java.io.IOException
import javax.inject.Singleton

@Singleton
class ConfigController {
    private val pathPlaceholders: MutableMap<String, String> = HashMap()
    private val extensionToFormat: MutableMap<String, ConfigFormat> = HashMap()
    private val defaultFormat: ConfigFormat = ConfigFormat.YAML

    init {
        registerFormat(ConfigFormat.YAML)
        registerFormat(ConfigFormat.JSON)
        registerPlaceholder("lactea:", "lactea/config/")
    }

    private fun registerPlaceholder(name: String, replacement: String) {
        pathPlaceholders[name] = replacement
    }

    private fun registerFormat(format: ConfigFormat) {
        extensionToFormat[format.extension] = format
    }

    private fun acceptPlaceholders(name: String): String {
        var name = name
        for ((key, value) in pathPlaceholders) {
            name = name.replace(key, value)
        }
        return name
    }

    fun formatByExtensionOrDefault(extension: String) = extensionToFormat.getOrDefault(extension, defaultFormat)

    @Throws(IOException::class)
    fun <T> loadConfig(name: String, clazz: Class<T>): T {
        val file = File(acceptPlaceholders(name))
        val parent = file.absoluteFile.parentFile
        parent.mkdirs()
        val candidates = parent.listFiles()
        check(candidates != null) {"$file is not a Directory"}
        for (candidate in candidates) {
            if (candidate.isDirectory) {
                continue
            }
            if (candidate.nameWithoutExtension == file.name) {
                return formatByExtensionOrDefault(candidate.extension).loadConfig(candidate, clazz)
            }
        }
        return createDefault(parent, file.name, clazz)
    }

    @Throws(IOException::class)
    private fun <T> createDefault(parent: File, name: String, clazz: Class<T>): T {
        val format = defaultFormat
        val file = File(parent, name + '.' + format.extension)
        format.objectMapper.writeValue(file, clazz.newInstance())
        file.createNewFile()
        return format.loadConfig(file, clazz)
    }
}
