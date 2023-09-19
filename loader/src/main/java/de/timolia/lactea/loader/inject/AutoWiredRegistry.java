package de.timolia.lactea.loader.inject;

import de.timolia.lactea.loader.module.InternalModuleAccess;
import de.timolia.lactea.loader.module.LacteaModule;
import de.timolia.lactea.loader.module.discovery.DiscoveryClass;
import de.timolia.lactea.loader.module.discovery.DiscoveryIndex;
import java.lang.annotation.Annotation;
import java.util.Collection;

public interface AutoWiredRegistry<I> extends InstanceRegistry<I> {
    Class<? extends Annotation> autoWireAnnotation();

    default void performAutoWire(LacteaModule module) {
        DiscoveryIndex index = module.getDescription().getDiscoveryIndex();
        ModuleInjector injector = InternalModuleAccess.getInjector(module);
        Collection<DiscoveryClass> classes = index.runDiscovery(autoWireAnnotation());
        for (DiscoveryClass discoveryClass : classes) {
            try {
                //noinspection unchecked
                Class<? extends I> javaClass = (Class<? extends I>) discoveryClass.loadClass();
                register(injector.getInstance(InjectedInstance.ofClass(javaClass)));
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
