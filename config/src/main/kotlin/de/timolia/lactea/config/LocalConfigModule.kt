package de.timolia.lactea.config

import com.google.inject.AbstractModule
import java.io.IOException
import java.util.logging.Level

class LocalConfigModule(
) : AbstractModule() {

    /*private fun <T> createConfigObject(name: String, clazz: Class<T>): T? {
        return try {
            controller.loadConfig<T>(name, clazz)
        } catch (e: IOException) {
            logger.log(Level.WARNING, "Failed to load config with name $name as $clazz", e)
            try {
                clazz.getConstructor().newInstance()
            } catch (ex: Exception) {
                logger.log(Level.WARNING, "Failed to create dummy config object", ex)
                null
            }
        }
    }

    private fun <T> bindConfig(name: String, clazz: Class<T>) {
        bind<T>(clazz).toInstance(createConfigObject<T>(name, clazz))
    }*/
}
