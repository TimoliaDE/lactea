package de.timolia.lactea.loader.startup;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import de.timolia.lactea.loader.module.LacteaModule;
import de.timolia.lactea.loader.startup.internal.ModuleLoadContext;
import java.util.ArrayList;
import java.util.List;

/**
 * @author David (_Esel)
 */
public class StartUpController {
    private final List<Module> baseModules = new ArrayList<>();


    public ModuleLoadContext loadModule(LacteaModule module) throws Exception {
        ModuleLoadContext loadContext = loadContext(module);
        module.onLoad(loadContext);
        return loadContext;
    }

    public ModuleLoadContext loadContext(LacteaModule module) {
        return new ModuleLoadContext(this, module);
    }

    public EnableContext enableContext(LacteaModule module) {
        return new EnableContext() {};
    }

    private Injector createGlobal() {
        return Guice.createInjector(baseModules);
    }

    public void initializeInjectors(List<ModuleLoadContext> contexts) {
        Injector global = createGlobal();
        contexts.forEach(loadContext -> loadContext.fullModuleInitialization(global));
    }

    public void addGlobalModule(Module module) {
        baseModules.add(module);
    }
}
