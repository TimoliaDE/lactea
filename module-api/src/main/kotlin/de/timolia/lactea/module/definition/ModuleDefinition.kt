package de.timolia.lactea.module.definition

@Target(AnnotationTarget.CLASS)
annotation class ModuleDefinition(
    val value: String,
    val dependencies: Array<Dependency> = []
)
