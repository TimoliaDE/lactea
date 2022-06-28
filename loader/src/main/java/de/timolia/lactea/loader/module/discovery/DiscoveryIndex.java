package de.timolia.lactea.loader.module.discovery;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import java.io.DataInputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;

/**
 * @author David (_Esel)
 */
public class DiscoveryIndex {
    private final Multimap<String, DiscoveryClass> index = HashMultimap.create();

    void addToIndex(DiscoveryClass discoveryClass) {
        for (javassist.bytecode.annotation.Annotation annotation : discoveryClass.getAnnotations()) {
            index.put(annotation.getTypeName(), discoveryClass);
        }
    }

    public void indexJarFile(JarFile jar) throws IOException {
        Enumeration<JarEntry> entries = jar.entries();
        while (entries.hasMoreElements()) {
            JarEntry jarEntry = entries.nextElement();
            if (jarEntry.getName().endsWith(".class")) {
                ClassFile classFile = new ClassFile(new DataInputStream(jar.getInputStream(jarEntry)));
                AnnotationsAttribute visible = (AnnotationsAttribute) classFile.getAttribute("RuntimeVisibleAnnotations");
                if (visible != null) {
                    addToIndex(new DiscoveryClass(classFile.getName(), visible.getAnnotations()));
                }
            }
        }
    }


    public Collection<DiscoveryClass> runDiscovery(String search) {
        return index.get(search);
    }

    public Collection<DiscoveryClass> runDiscovery(Class<? extends Annotation> search) {
        return runDiscovery(search.getName());
    }
}
