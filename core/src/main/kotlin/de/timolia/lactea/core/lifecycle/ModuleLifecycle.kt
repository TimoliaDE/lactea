package de.timolia.lactea.core.lifecycle

import de.timolia.lactea.core.module.ModuleRepository
import de.timolia.lactea.lifecycle.EnableContext
import de.timolia.lactea.source.ClassPathInjector
import java.util.logging.Level
import java.util.logging.Logger
import javax.inject.Inject

class ModuleLifecycle @Inject constructor (
    private val logger: Logger,
    private val classPathInjector: ClassPathInjector,
    private val moduleRepository: ModuleRepository,
    private val moduleMainLoader: ModuleMainLoader
) {
    private fun addToClassPath() {
        moduleRepository.modulesInBootOrder.forEach {
            classPathInjector.addToClassPath(it.uri)
        }
    }

    private fun createInstances() {
        moduleRepository.modulesInBootOrder.forEach {
            try {
                it.instance = moduleMainLoader.load(it)
            } catch (ex: Throwable) {
                logger.log(Level.SEVERE, "Failed to load " + it.nameAndLocation(), ex)
            }
        }
    }

    fun addToClassPathAndCreateInstances() {
        addToClassPath()
        createInstances()
    }

    fun enableAll() {
        for (module in moduleRepository.modulesInBootOrder) {
            try {
                module.instance.onEnable(ModuleEnableContext())
            } catch (ex: Exception) {
                logger.log(Level.SEVERE, "Failed to enable $module", ex)
            }
        }
    }

    internal class ModuleEnableContext : EnableContext

    fun disableAll() {
        val orderedModules = moduleRepository.modulesInBootOrder
        for (module in orderedModules) {
            try {
                module.instance.onDisable()
            } catch (ex: Exception) {
                logger.log(Level.SEVERE, "Failed to disable $module", ex)
            }
        }
        for (module in orderedModules) {
            try {
                module.instance.onPostDisable()
            } catch (ex: Exception) {
                logger.log(Level.SEVERE, "Failed to post disable $module", ex)
            }
        }
    }
}