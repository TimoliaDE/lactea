package de.timolia.lactea.loader.startup.internal;

import com.google.inject.Injector;
import com.google.inject.Module;
import de.timolia.lactea.loader.module.InternalModuleAccess;
import de.timolia.lactea.loader.module.LacteaModule;
import de.timolia.lactea.loader.startup.LoadContext;
import de.timolia.lactea.loader.startup.StartUpController;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

/**
 * @author David (_Esel)
 */
@RequiredArgsConstructor
public class ModuleLoadContext implements LoadContext {
    private final StartUpController controller;
    private final LacteaModule module;
    private final List<Function<Injector, Module>> localModules = new ArrayList<>();

    public void fullModuleInitialization(Injector global) {
        Injector injector = createInjector(global);
        InternalModuleAccess.setInjector(module, injector);
    }

    private Injector createInjector(Injector global) {
        return global.createChildInjector(localModules.stream()
            .map(function -> function.apply(global))
            .collect(Collectors.toList()));
    }

    @Override
    public void installGlobalModule(Module module) {
        controller.addGlobalModule(module);
    }

    @Override
    public void installModule(Module module) {
        localModules.add(injector -> module);
    }

    @Override
    public void installModule(Class<? extends Module> moduleClass) {
        localModules.add(injector -> injector.getInstance(moduleClass));
    }
}
