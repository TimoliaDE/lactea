package de.timolia.lactea.core

import com.google.inject.Guice
import com.google.inject.Injector
import com.google.inject.Module
import de.timolia.lactea.core.lifecycle.DIConfiguration
import de.timolia.lactea.core.lifecycle.ModuleLifecycle
import de.timolia.lactea.core.lifecycle.StartUp
import de.timolia.lactea.path.PathResolver
import de.timolia.lactea.source.ClassPathInjector

class Lactea (
    classPathInjector: ClassPathInjector,
    pathResolver: PathResolver,
    vararg modules: Module,
) {
    val injector: Injector

    init {
        injector = Guice.createInjector(
            *modules,
            Module {
                it.bind(ClassPathInjector::class.java) to classPathInjector
                it.bind(PathResolver::class.java) to pathResolver
            }
        )
    }

    fun start() = injector.getInstance(StartUp::class.java).performStartup()
}