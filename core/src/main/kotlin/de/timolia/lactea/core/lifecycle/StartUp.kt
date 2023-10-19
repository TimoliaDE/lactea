package de.timolia.lactea.core.lifecycle

import javax.inject.Inject

class StartUp @Inject constructor(
    private val diConfiguration: DIConfiguration,
    private val moduleLifecycle: ModuleLifecycle
) {
    fun performStartup() {
        diConfiguration.setupDependencyInjection()
        moduleLifecycle.enableAll()
    }
}