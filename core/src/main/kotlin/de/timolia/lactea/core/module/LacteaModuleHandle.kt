package de.timolia.lactea.core.module

import de.timolia.lactea.core.discovery.DiscoveryIndex
import de.timolia.lactea.module.LacteaModule
import java.io.File
import java.net.URI
import java.util.jar.JarFile

class LacteaModuleHandle (
    val uri: URI,
    val jar: JarFile,
    val discoveryIndex: DiscoveryIndex,
    val description: ModuleDescription
) {
    lateinit var instance: LacteaModule

    fun nameAndLocation(): String {
        return description.name + " in " + uri.toString()
    }

    fun name() = description.name

    companion object {
        fun fromUri(uri: URI): LacteaModuleHandle {
            val scheme = uri.scheme
            if ("file".equals(scheme, true)) {
                return fromFile(File(uri.path))
            }
            throw UnsupportedOperationException("$scheme is currently not supported")
        }

        fun fromFile(file: File): LacteaModuleHandle {
            val jarFile = JarFile(file)
            val discoveryIndex = DiscoveryIndex()
            discoveryIndex.indexJarFile(jarFile)
            return LacteaModuleHandle(
                file.toURI(),
                jarFile,
                discoveryIndex,
                ModuleDescription(discoveryIndex)
            )
        }
    }
}