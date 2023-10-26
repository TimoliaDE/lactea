package de.timolia.lactea.lifecycle

import com.google.inject.Module;
import de.timolia.lactea.inject.InjectedInstance

interface LoadContext {
    fun installGlobalModule(module: InjectedInstance<Module>)
    fun installModule(module: InjectedInstance<Module>)

    var encapsulationMode: EncapsulationMode


    enum class EncapsulationMode {
        PRIVATE_MODULE,
        INJECTOR
    }
}
