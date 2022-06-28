package de.timolia.lactea.loader.module;

import com.google.inject.Injector;
import java.util.Objects;

/**
 * @author David (_Esel)
 */
public class InternalModuleAccess {
    public static void setInjector(LacteaModule module, Injector injector) {
        module.setInjector(Objects.requireNonNull(injector, "injector"));
    }
}
