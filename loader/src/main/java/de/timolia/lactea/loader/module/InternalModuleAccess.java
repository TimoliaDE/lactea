package de.timolia.lactea.loader.module;

import com.google.inject.Injector;
import de.timolia.lactea.loader.inject.ModuleInjector;
import java.util.Objects;

/**
 * @author David (_Esel)
 */
public class InternalModuleAccess {
    public static void setInjector(LacteaModule module, Injector injector) {
        Objects.requireNonNull(injector, "injector");
        ModuleInjector moduleInjector = new ModuleInjector(injector);
        module.setInjector(moduleInjector);
    }

    public static ModuleInjector getInjector(LacteaModule module) {
        return module.getInjector();
    }
}
