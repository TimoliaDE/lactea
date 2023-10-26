package de.timolia.lactea.core.module

import de.timolia.lactea.path.LacteaPathKeys
import de.timolia.lactea.path.PathResolver
import java.util.logging.Level
import java.util.logging.Logger
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ModuleRepository @Inject constructor(
    private val logger: Logger,
    pathResolver: PathResolver
) {
    private val modules = HashMap<String, LacteaModuleHandle>()
    val modulesInBootOrder: List<LacteaModuleHandle>

    init {
        val moduleRoot = pathResolver.resolve(LacteaPathKeys.MODULES)
        moduleRoot.forEachJar {
            try {
                register(LacteaModuleHandle.fromUri(it))
            } catch (exception: Exception) {
                logger.log(Level.WARNING, "Failed to read module at $it", exception)
            }
        }
        modulesInBootOrder = ModuleBootOrder(this).modules
    }

    fun modules(): Collection<LacteaModuleHandle> {
        return modules.values
    }

    fun byName(name: String): LacteaModuleHandle? {
        return modules[normalizeName(name)]
    }

    fun requireByName(name: String): LacteaModuleHandle {
        return byName(name) ?: throw IllegalStateException("No module with name $name")
    }

    private fun register(module: LacteaModuleHandle) {
        val name = normalizeName(module.description.name)
        if (modules.put(name, module) != null) {
            logger.warning("Duplicated definition for $name")
        }
    }

    private fun normalizeName(name: String): String {
        return name.lowercase()
    }
}
