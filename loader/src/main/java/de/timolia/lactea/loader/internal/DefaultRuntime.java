package de.timolia.lactea.loader.internal;

import de.timolia.lactea.loader.Runtime;
import de.timolia.lactea.loader.module.ModuleManager;
import de.timolia.lactea.loader.startup.StartUpController;
import de.timolia.lactea.loader.startup.internal.Integrity;
import de.timolia.lactea.loader.startup.internal.ModuleLoadContext;

import java.io.File;
import java.util.List;
import java.util.Objects;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author David (_Esel)
 */
@RequiredArgsConstructor
public class DefaultRuntime implements Runtime {
    private final ModuleManager moduleManager;
    @Getter
    private final StartUpController startUpController;

    public void loadLibraries() {
        moduleManager.loadLibraries();
    }

    public void initialize() {
        Integrity.checkIntegrity();
        postLibraries();
    }

    private void postLibraries() {
        moduleManager.scan();
        startUpController.addGlobalModule(binder -> binder.bind(Runtime.class).toInstance(this));
        List<ModuleLoadContext> contexts = moduleManager.loadAll(startUpController);
        startUpController.initializeInjectors(contexts);
    }

    public void enable() {
        moduleManager.enableAll(startUpController);
    }

    public void shutdown() {
        moduleManager.disableAll();
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

    public static DefaultRuntime create(File fileRoot) {
        ModuleManager moduleManager = new ModuleManager(fileRoot);
        return new DefaultRuntime(moduleManager, new StartUpController());
    }
}
