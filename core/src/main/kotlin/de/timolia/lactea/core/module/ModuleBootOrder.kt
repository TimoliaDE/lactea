package de.timolia.lactea.core.module

import de.timolia.lactea.core.module.ModuleDescription.DependencyDescription
import java.util.*
import java.util.stream.Collectors

internal class ModuleBootOrder (
    private val registry: ModuleRepository,
) {
    private val orderedModules: MutableList<LacteaModuleHandle> = ArrayList()
    val modules: List<LacteaModuleHandle> = orderedModules
    private val trace = Stack<LacteaModuleHandle>()

    init {
        registry.modules().sortedBy(LacteaModuleHandle::name).forEach(::tryAdd)
    }

    private fun handleMissingDependency(module: DependencyDescription) {
        check(!module.required) { "Missing dependency " + module.value }
    }

    private fun tryAdd(module: LacteaModuleHandle) {
        try {
            add(module)
        } catch (exception: Exception) {
            throw RuntimeException("Unable to build dependency tree for" + module.name())
        }
    }

    private fun currentState(module: LacteaModuleHandle): State {
        if (trace.contains(module)) {
            return State.IN_LOAD_TRACE
        }
        return if (orderedModules.contains(module)) {
            State.SAFELY_LOADED
        } else State.NOT_INVOLVED
    }

    private fun handleCircularDependency() {
        val formatted = trace.stream()
            .map(LacteaModuleHandle::name)
            .collect(Collectors.joining(" <-> "))
        throw RuntimeException("Circular dependency between $formatted")
    }

    private fun addNext(module: LacteaModuleHandle) {
        orderedModules.add(module)
        for (dependencyDescription in module.description.dependencies) {
            val dependency = registry.byName(dependencyDescription.value)
            if (dependency == null) {
                handleMissingDependency(dependencyDescription)
                continue
            }
            tryAdd(dependency)
        }
        trace.pop()
    }

    private fun add(module: LacteaModuleHandle) {
        val state = currentState(module)
        trace.push(module)
        when (state) {
            State.IN_LOAD_TRACE -> handleCircularDependency()
            State.NOT_INVOLVED -> addNext(module)
            State.SAFELY_LOADED -> {}
        }
    }

    internal enum class State {
        IN_LOAD_TRACE,
        SAFELY_LOADED,
        NOT_INVOLVED
    }
}
