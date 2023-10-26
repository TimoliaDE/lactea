package de.timolia.lactea.module

import de.timolia.lactea.inject.LacteaInjector

object InternalModuleAccess {
    fun setInjector(module: LacteaModule, injector: LacteaInjector) {
        module.injector = injector
    }
}