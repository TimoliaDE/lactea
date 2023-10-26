package de.timolia.lactea.inject

interface InstanceRegistry<I> {
    fun register(instance: I)
    fun unregister(instance: I)
}
