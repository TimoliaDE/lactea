package de.timolia.lactea.loader.internal;

import de.timolia.lactea.loader.Runtime;
import de.timolia.lactea.loader.module.ModuleManager;
import de.timolia.lactea.loader.startup.StartUpController;
import de.timolia.lactea.loader.startup.internal.ModuleLoadContext;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;

/**
 * @author David (_Esel)
 */
@RequiredArgsConstructor
public class DefaultRuntime implements Runtime {
    private final ModuleManager moduleManager;
    private final StartUpController startUpController;

    public void initialize() {
        moduleManager.loadLibraries();
        moduleManager.scan();
        List<ModuleLoadContext> contexts = moduleManager.loadAll(startUpController);
        startUpController.initializeInjectors(contexts);
    }

    public void enable() {
        moduleManager.enableAll(startUpController);
    }

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
