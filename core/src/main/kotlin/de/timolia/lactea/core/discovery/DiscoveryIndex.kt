package de.timolia.lactea.core.discovery

import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap
import javassist.bytecode.AnnotationsAttribute
import javassist.bytecode.ClassFile
import java.io.DataInputStream
import java.io.IOException
import java.util.*
import java.util.jar.JarEntry
import java.util.jar.JarFile

class DiscoveryIndex {
    private val index: Multimap<String, DiscoveryClass> = HashMultimap.create()

    fun addToIndex(discoveryClass: DiscoveryClass) {
        for (annotation in discoveryClass.annotations) {
            index.put(annotation.typeName, discoveryClass)
        }
    }

    @Throws(IOException::class)
    fun indexJarFile(jar: JarFile) {
        val entries: Enumeration<JarEntry> = jar.entries()
        while (entries.hasMoreElements()) {
            val jarEntry: JarEntry = entries.nextElement()
            if (jarEntry.name.endsWith(".class")) {
                val classFile = ClassFile(DataInputStream(jar.getInputStream(jarEntry)))
                val visible: AnnotationsAttribute = classFile.getAttribute("RuntimeVisibleAnnotations") as AnnotationsAttribute
                if (visible != null) {
                    addToIndex(DiscoveryClass(classFile.name, visible.annotations))
                }
            }
        }
    }

    fun runDiscovery(search: String) = DiscoveryResult(index[search])

    fun runDiscovery(search: Class<out Annotation>) = runDiscovery(search.name)
}
