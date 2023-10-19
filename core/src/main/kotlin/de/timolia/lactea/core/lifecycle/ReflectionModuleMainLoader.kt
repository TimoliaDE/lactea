package de.timolia.lactea.core.lifecycle

import de.timolia.lactea.core.module.LacteaModuleHandle
import de.timolia.lactea.module.LacteaModule

class ReflectionModuleMainLoader : ModuleMainLoader {
    override fun load(module: LacteaModuleHandle): LacteaModule {
        val main = loadMainClass(module)
        try {
            return main.getConstructor().newInstance()
        } catch (exception: NoSuchMethodException) {
            throw MainInstanceCreation("Require a public no args constructor in " + mainClassName(module), exception)
        } catch (exception: Exception) {
            throw MainInstanceCreation("Failed to instantiate " + mainClassName(module), exception)
        }
    }

    private fun loadMainClass(module: LacteaModuleHandle): Class<out LacteaModule> {
        try {
            return module.description.loadMainClass()
        } catch (exception: ClassNotFoundException) {
            throw MainClassLoad("Unable to find " + mainClassName(module) + " in class path", exception)
        }
    }

    private fun mainClassName(module: LacteaModuleHandle) = module.description.main.name

    class MainClassLoad(message: String, cause: Throwable) : RuntimeException(message, cause)
    class MainInstanceCreation(message: String, cause: Throwable) : RuntimeException(message, cause)
}