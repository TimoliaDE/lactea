package de.timolia.lactea.core.lifecycle.startup

import de.timolia.lactea.core.lifecycle.DIConfiguration
import de.timolia.lactea.core.lifecycle.ModuleLifecycle
import javax.inject.Inject

class StartUp @Inject constructor(
    private val diConfiguration: DIConfiguration,
    private val moduleLifecycle: ModuleLifecycle
) : LoadEnableSplitStartup, SingleEntrypointStartup {
    override fun performStartup() {
        setupDependencyInjection()
        enableModules()
    }

    override fun setupDependencyInjection() = diConfiguration.setupDependencyInjection()

    override fun enableModules() = moduleLifecycle.enableAll()
}