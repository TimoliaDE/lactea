package de.timolia.lactea.core.discovery

import javassist.bytecode.annotation.Annotation as ByteCodeAnnotation

class DiscoveryClass(
    val name: String,
    val annotations: Array<ByteCodeAnnotation>
) {
    private val byType: MutableMap<String, ByteCodeAnnotation> = HashMap()

    init {
        buildTypeIndex()
    }

    private fun buildTypeIndex() {
        for (annotation in annotations) {
            check(byType.put(annotation.getTypeName(), annotation) == null) { "Illegal discovery name=$name annotation=$annotation" }
        }
    }

    fun byType(search: String): ByteCodeAnnotation? {
        return byType[search]
    }

    fun byType(search: Class<out Annotation>): ByteCodeAnnotation? {
        return byType(search.name)
    }

    @Throws(ClassNotFoundException::class)
    fun loadClass(): Class<*> {
        return Class.forName(name)
    }
}
