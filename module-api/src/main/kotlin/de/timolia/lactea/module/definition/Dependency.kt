package de.timolia.lactea.module.definition

@Target
annotation class Dependency(
    val value: String,
    val required: Boolean = true
)
