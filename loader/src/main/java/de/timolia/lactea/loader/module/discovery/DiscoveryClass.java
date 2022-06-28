package de.timolia.lactea.loader.module.discovery;

import de.timolia.lactea.loader.module.LacteaModule;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javassist.bytecode.annotation.Annotation;
import lombok.Getter;
import lombok.ToString;

/**
 * @author David (_Esel)
 */
@Getter
@ToString(exclude = "byType")
public class DiscoveryClass {
    private final String name;
    private final Annotation[] annotations;
    private final Map<String, Annotation> byType = new HashMap<>();


    public DiscoveryClass(String name, Annotation[] annotations) {
        this.name = name;
        this.annotations = annotations;
        buildTypeIndex();
    }

    private void buildTypeIndex() {
        for (Annotation annotation : annotations) {
            if (byType.put(annotation.getTypeName(), annotation) != null) {
                throw new IllegalStateException("Illegal discovery name=" + name + " annotation=" + annotation);
            }
        }
    }

    public Annotation byType(String search) {
        return byType.get(search);
    }

    public Annotation byType(Class<? extends java.lang.annotation.Annotation> search) {
        return byType(search.getName());
    }

    public Class<? extends LacteaModule> loadClass() throws ClassNotFoundException {
        return (Class<? extends LacteaModule>) Class.forName(name);
    }
}
