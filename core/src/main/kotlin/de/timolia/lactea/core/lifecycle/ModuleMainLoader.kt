package de.timolia.lactea.core.lifecycle

import de.timolia.lactea.core.module.LacteaModuleHandle
import de.timolia.lactea.module.LacteaModule

interface ModuleMainLoader {
    fun load(module: LacteaModuleHandle): LacteaModule
}