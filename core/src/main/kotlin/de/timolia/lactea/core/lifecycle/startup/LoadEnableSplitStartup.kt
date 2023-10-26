package de.timolia.lactea.core.lifecycle.startup

interface LoadEnableSplitStartup {
    fun setupDependencyInjection();

    fun enableModules();
}