package de.timolia.lactea.loader.internal;

import de.timolia.lactea.loader.Runtime;
import de.timolia.lactea.loader.module.ModuleManager;
import java.util.Objects;
import lombok.RequiredArgsConstructor;

/**
 * @author David (_Esel)
 */
@RequiredArgsConstructor
public class DefaultRuntime implements Runtime {
    private final ModuleManager moduleManager;

    @Override
    public <MT> MT getModule(String name) {
        Objects.requireNonNull(name, "name");
        return (MT) moduleManager.byName(name);
    }

    @Override
    public boolean isModuleLoaded(String name) {
        return moduleManager.isLoaded(Objects.requireNonNull(name, "name"));
    }
}
