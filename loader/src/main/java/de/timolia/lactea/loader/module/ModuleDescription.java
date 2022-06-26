package de.timolia.lactea.loader.module;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.StringMemberValue;
import lombok.Getter;

/**
 * @author David (_Esel)
 */
@Getter
public class ModuleDescription {
    private final File file;
    private String name;
    private String main;

    public ModuleDescription(File file, JarFile jar) throws IOException {
        this.file = file;
        Enumeration<JarEntry> jarEntries = jar.entries();
        while (jarEntries.hasMoreElements()) {
            JarEntry jarEntry = jarEntries.nextElement();
            if (jarEntry.getName().endsWith(".class")) {
                ClassFile classFile = new ClassFile(new DataInputStream(jar.getInputStream(jarEntry)));
                AnnotationsAttribute visible = (AnnotationsAttribute) classFile.getAttribute("RuntimeVisibleAnnotations");
                if (visible != null) {
                    for (Annotation annotation : visible.getAnnotations()) {
                        if (ModuleDefinition.class.getName().equals(annotation.getTypeName())) {
                            main = classFile.getName();
                            name = ((StringMemberValue) annotation.getMemberValue("value")).getValue();
                        }
                    }
                }
            }
        }
    }

    public URL url() throws MalformedURLException {
        return file.toURI().toURL();
    }
}
