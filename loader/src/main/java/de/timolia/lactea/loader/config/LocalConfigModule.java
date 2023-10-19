package de.timolia.lactea.loader.config;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.inject.AbstractModule;
import de.timolia.lactea.loader.internal.JavassistAnnotations;
import de.timolia.lactea.loader.module.LacteaModule;
import de.timolia.lactea.loader.module.discovery.DiscoveryClass;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author David (_Esel)
 */
@AutoFactory
public class LocalConfigModule extends AbstractModule {
    private final Logger logger;
    private final ConfigController controller;
    private final LacteaModule module;

    public LocalConfigModule(@Provided Logger logger,
                             @Provided ConfigController controller,
                             LacteaModule module) {
        this.logger = logger;
        this.controller = controller;
        this.module = module;
    }

    private <T> T createConfigObject(String name, Class<T> clazz) {
        try {
            return controller.loadConfig(name, clazz);
        } catch (IOException e) {
            logger.log(Level.WARNING, "Failed to load config with name " + name + " as " + clazz, e);
            try {
                return clazz.getConstructor().newInstance();
            } catch (Exception ex) {
                logger.log(Level.WARNING, "Failed to create dummy config object", ex);
                return null;
            }
        }
    }

    private <T> void bindConfig(String name, Class<T> clazz) {
        bind(clazz).toInstance(createConfigObject(name, clazz));
    }
}
