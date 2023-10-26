package de.timolia.lactea.core.module

import de.timolia.lactea.core.module.ModuleDescription.DependencyDescription
import javassist.bytecode.annotation.*
import javassist.bytecode.annotation.Annotation

internal object JavassistAnnotations {
    fun stringValue(annotation: Annotation, name: String): String {
        val value: StringMemberValue = annotation.getMemberValue(name) as StringMemberValue
        return value.value
    }

    fun dependencyDescriptions(annotation: Annotation, name: String): Array<DependencyDescription> {
        val value: ArrayMemberValue = annotation.getMemberValue(name) as ArrayMemberValue
        return value.value.map {
            val dependencyAnnotation: Annotation = (it as AnnotationMemberValue).value
            DependencyDescription(
                stringValue(dependencyAnnotation, "value"),
                (dependencyAnnotation.getMemberValue("required") as BooleanMemberValue).value
            )
        }.toTypedArray()
    }
}
