package de.timolia.lactea.core.lifecycle

import com.google.inject.Injector
import de.timolia.lactea.core.module.LacteaModuleHandle
import de.timolia.lactea.core.module.ModuleRepository
import de.timolia.lactea.inject.InjectedInstance
import de.timolia.lactea.lifecycle.LoadContext
import de.timolia.lactea.module.InternalModuleAccess
import java.io.Closeable
import java.util.logging.Level
import java.util.logging.Logger
import javax.inject.Inject
import com.google.inject.Module
import com.google.inject.PrivateModule
import de.timolia.lactea.inject.LacteaInjector
import de.timolia.lactea.lifecycle.LoadContext.EncapsulationMode
import javax.inject.Provider

class DIConfiguration @Inject constructor (
    private val moduleLifecycle: ModuleLifecycle,
    private val moduleRepository: ModuleRepository,
    private val configurationInjector: Injector,
    private val logger: Logger
) {
    private val globalInjectorConfiguration = InjectorConfiguration()

    fun setupDependencyInjection() {
        moduleLifecycle.addToClassPathAndCreateInstances()
        val contexts = buildModuleContextList()
        preInjectorCreation(contexts)
        val globalInjector = globalInjectorConfiguration.create(configurationInjector)
        postInjectionCreation(contexts, globalInjector)
    }

    private fun postInjectionCreation(contexts: List<ModuleLoadContext>, injector: Injector) {
        contexts.forEach {
            if (it.encapsulationMode == EncapsulationMode.INJECTOR) {
                it.localConfiguration.create(injector)
            }
            InternalModuleAccess.setInjector(it.module.instance, LacteaInjector(it.localConfiguration.moduleInjector!!.get()))
        }
    }

    private fun preInjectorCreation(contexts: List<ModuleLoadContext>) {
        contexts.forEach {
            if (it.encapsulationMode == EncapsulationMode.PRIVATE_MODULE) {
                globalInjectorConfiguration.install(InjectedInstance.ofInstance(
                    it.localConfiguration.createPrivateModule(configurationInjector)
                ))
            }
        }
    }

    private fun buildModuleContextList(): List<ModuleLoadContext> {
        return moduleRepository.modulesInBootOrder.mapNotNull(::buildModuleContext)
    }

    private fun buildModuleContext(module: LacteaModuleHandle): ModuleLoadContext? {
        val context = ModuleLoadContext(module)
        context.use {
            try {
                module.instance.onInjectorConfiguration(it)
                return context
            } catch (throwable: Throwable) {
                logger.log(Level.WARNING, "Failed to load injection configuration for ${module.nameAndLocation()}", throwable)
                return null
            }
        }
    }

    internal class InjectorConfiguration(
        private val moduleConfiguration: MutableList<InjectedInstance<Module>> = mutableListOf()
    ) {
        var moduleInjector: Provider<Injector>? = null

        fun create(parent: Injector): Injector {
            val injector = parent.createChildInjector(createModules(parent))
            moduleInjector = Provider { injector }
            return injector
        }

        private fun createModules(injector: Injector) = moduleConfiguration.map { it.getInstance(injector) }

        fun install(module: InjectedInstance<Module>) = moduleConfiguration.add(module)

        fun createPrivateModule(injector: Injector) = object: PrivateModule() {
            override fun configure() {
                createModules(injector).forEach(::install)
                moduleInjector = getProvider(Injector::class.java)
            }
        }
    }

    internal inner class ModuleLoadContext (
        val module: LacteaModuleHandle,
        override var encapsulationMode: EncapsulationMode = EncapsulationMode.PRIVATE_MODULE
    ) : LoadContext, Closeable {
        val localConfiguration = InjectorConfiguration()
        private val thread: Thread = Thread.currentThread()
        private var closed = false

        override fun installGlobalModule(module: InjectedInstance<Module>) {
            validateContext()
            globalInjectorConfiguration.install(module)
        }

        override fun installModule(module: InjectedInstance<Module>) {
            validateContext()
            localConfiguration.install(module)
        }

        override fun close() {
            validateContext()
            closed = true
        }

        private fun validateContext() {
            val currentThread = Thread.currentThread()
            if (thread != currentThread) {
                throw IllegalStateException(
                    "Called from wrong thread."
                            + "Expecting ${thread.name} got ${currentThread.name}"
                )
            }
            if (closed) {
                throw IllegalStateException("This module configuration phase is already over")
            }
        }
    }
}