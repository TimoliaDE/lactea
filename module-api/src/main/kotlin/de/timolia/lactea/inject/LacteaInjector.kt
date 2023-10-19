package de.timolia.lactea.inject

import com.google.inject.Injector

class LacteaInjector (
    private val injector: Injector
) {
    fun <T> getInstance(instance: InjectedInstance<T>): T {
        return instance.getInstance(injector)
    }
}
