package de.timolia.lactea.core.module

import de.timolia.lactea.core.discovery.DiscoveryClass
import de.timolia.lactea.core.discovery.DiscoveryIndex
import de.timolia.lactea.module.LacteaModule
import de.timolia.lactea.module.definition.ModuleDefinition
import javassist.bytecode.annotation.Annotation

class ModuleDescription(
    discoveryIndex: DiscoveryIndex
) {
    val main: DiscoveryClass
    val name: String
    val dependencies: Array<DependencyDescription>

    init {
        val candidates: Collection<DiscoveryClass> = discoveryIndex.runDiscovery(ModuleDefinition::class.java)
        check(candidates.size == 1) {
            ("Require exactly one Module definition."
                    + " Candidates are: " + candidates)
        }
        main = candidates.iterator().next()
        val definition: Annotation = main.byType(ModuleDefinition::class.java)!!
        name = JavassistAnnotations.stringValue(definition, "value")
        dependencies = try {
            JavassistAnnotations.dependencyDescriptions(definition, "dependencies")
        } catch (exception: Exception) {
            throw IllegalStateException("Failed to parse dependencies", exception)
        }
    }

    @Throws(ClassNotFoundException::class)
    fun loadMainClass(): Class<out LacteaModule> {
        return main.loadClass() as Class<out LacteaModule>
    }

    data class DependencyDescription(val value: String, val required: Boolean)
}
