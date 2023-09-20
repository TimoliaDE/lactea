package de.timolia.lactea.loader.module;

import de.timolia.lactea.loader.module.definition.ModuleDescription;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

public class ModuleRegistry {
    private static final Logger LOGGER = Logger.getLogger(ModuleRegistry.class.getName());

    final Map<String, ModuleDescription> definitions = new HashMap<>();
    final Collection<ModuleDescription> immutableDefinitionView = Collections.unmodifiableCollection(definitions.values());
    final Map<String, LacteaModule> modules = new HashMap<>();

    public Collection<ModuleDescription> definitions() {
        return immutableDefinitionView;
    }

    public LacteaModule byName(String name) {
        return modules.get(normalizeName(name));
    }

    public LacteaModule requireByName(String name) {
        LacteaModule module = byName(name);
        if (module == null) {
            throw new IllegalStateException("No module with name " + name);
        }
        return module;
    }

    public ModuleDescription definition(String name) {
        return definitions.get(normalizeName(name));
    }

    void addDefinition(ModuleDescription desc) {
        String name = normalizeName(desc.getName());
        if (definitions.put(name, desc) != null) {
            LOGGER.warning("Duplicated definition for " + name);
        }
    }

    void addModule(LacteaModule module) {
        String name = normalizeName(module.getDescription().getName());
        if (!definitions.containsKey(name)) {
            throw new IllegalStateException("No definition for module " + name);
        }
        if (modules.put(name, module) != null) {
            throw new IllegalStateException("Module with name " + name + " already registered");
        }
    }

    private String normalizeName(String name) {
        return name.toLowerCase(Locale.ROOT);
    }
}
