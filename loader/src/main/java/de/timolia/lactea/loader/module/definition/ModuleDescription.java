package de.timolia.lactea.loader.module.definition;

import de.timolia.lactea.loader.internal.JavassistAnnotations;
import de.timolia.lactea.loader.module.LacteaModule;
import de.timolia.lactea.loader.module.discovery.DiscoveryClass;
import de.timolia.lactea.loader.module.discovery.DiscoveryIndex;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.jar.JarFile;
import javassist.bytecode.annotation.Annotation;
import lombok.Getter;

/**
 * @author David (_Esel)
 */
@Getter
public class ModuleDescription {
    private final File file;
    private final DiscoveryIndex discoveryIndex = new DiscoveryIndex();
    private final DiscoveryClass main;
    private final String name;
    private final DependencyDescription[] dependencies;

    public ModuleDescription(File file, JarFile jar) throws IOException {
        this.file = file;
        discoveryIndex.indexJarFile(jar);
        Collection<DiscoveryClass> candidates = discoveryIndex.runDiscovery(ModuleDefinition.class);
        if (candidates.size() != 1) {
            throw new IllegalStateException("Require exactly one Module definition."
                + " Candidates are: " + candidates);
        }
        main = candidates.iterator().next();
        Annotation definition = main.byType(ModuleDefinition.class);
        name = JavassistAnnotations.stringValue(definition, "value");
        try {
            dependencies = JavassistAnnotations.dependencyDescriptions(definition, "dependencies");
        } catch (Exception exception) {
            throw new IllegalStateException("Failed to parse dependencies", exception);
        }
    }

    public String nameAndLocation() {
        return name + " in " + file.getName();
    }

    public URL url() throws MalformedURLException {
        return file.toURI().toURL();
    }

    public Class<? extends LacteaModule> mainClass() throws ClassNotFoundException {
        return (Class<? extends LacteaModule>) main.loadClass();
    }

    public record DependencyDescription(String value, boolean required) {

    }
}
