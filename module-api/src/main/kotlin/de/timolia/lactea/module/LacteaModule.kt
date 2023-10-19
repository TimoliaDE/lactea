package de.timolia.lactea.module

import de.timolia.lactea.inject.LacteaInjector
import de.timolia.lactea.lifecycle.EnableContext
import de.timolia.lactea.lifecycle.LoadContext

class LacteaModule {
    internal lateinit var injector: LacteaInjector

    @Throws(Exception::class)
    fun onInjectorConfiguration(context: LoadContext) {
    }

    @Throws(Exception::class)
    fun onEnable(context: EnableContext) {
    }

    @Throws(Exception::class)
    fun onDisable() {
    }

    @Throws(Exception::class)
    fun onPostDisable() {
    }
}
