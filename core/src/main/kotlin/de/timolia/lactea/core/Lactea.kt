package de.timolia.lactea.core

import com.google.inject.Guice
import com.google.inject.Injector
import com.google.inject.Module
import de.timolia.lactea.Bootstrap
import de.timolia.lactea.core.lifecycle.startup.LoadEnableSplitStartup
import de.timolia.lactea.core.lifecycle.startup.SingleEntrypointStartup
import de.timolia.lactea.core.lifecycle.startup.StartUp
import de.timolia.lactea.path.PathResolver
import de.timolia.lactea.source.ClassPathInjector

class Lactea (
    bootstrap: Bootstrap,
    vararg modules: Module,
) {
    val injector: Injector

    init {
        injector = Guice.createInjector(
            *modules,
            Module {
                it.bind(ClassPathInjector::class.java) to bootstrap.classPathInjector()
                it.bind(PathResolver::class.java) to bootstrap.pathResolver()
            }
        )
    }

    fun singleEntrypoint(): SingleEntrypointStartup = injector.getInstance(StartUp::class.java)

    fun loadEnableSplit(): LoadEnableSplitStartup = injector.getInstance(StartUp::class.java)
}