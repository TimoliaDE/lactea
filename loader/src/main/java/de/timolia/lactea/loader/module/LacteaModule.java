package de.timolia.lactea.loader.module;

import de.timolia.lactea.loader.inject.ModuleInjector;
import de.timolia.lactea.loader.startup.EnableContext;
import de.timolia.lactea.loader.startup.LoadContext;
import java.util.logging.Logger;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * @author David (_Esel)
 */
public class LacteaModule {
    @Setter(AccessLevel.PACKAGE)
    @Getter(AccessLevel.PROTECTED)
    private ModuleInjector injector;
    @Getter
    @Setter(AccessLevel.PACKAGE)
    private ModuleDescription description;
    @Getter(AccessLevel.PROTECTED)
    private final Logger logger = Logger.getLogger(getClass().getName());

    public void onEnable(EnableContext context) throws Exception {
    }

    public void onLoad(LoadContext context) throws Exception {
    }

    public void onDisable() throws Exception {

    }

    public void onPostDisable() throws Exception {
    }
}
