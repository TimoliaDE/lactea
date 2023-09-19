package de.timolia.lactea.loader.inject;

import com.google.inject.Injector;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ModuleInjector {
    private final Injector injector;

    public <T> T getInstance(InjectedInstance<T> instance) {
        return instance.getInstance(injector);
    }
}
