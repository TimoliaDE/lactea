package de.timolia.lactea.loader.inject;

import com.google.inject.Injector;
import java.util.Objects;
import java.util.function.Function;

/**
 * @author David (_Esel)
 */
public interface InjectedInstance<T> {
    static <T> InjectedInstance<T> ofInstance(T instance) {
        return injector -> instance;
    }

    static <T> InjectedInstance<T> ofClass(Class<? extends T> clazz) {
        Objects.requireNonNull(clazz, "class");
        return injector -> injector.getInstance(clazz);
    }

    static <T, F> InjectedInstance<T> ofFactory(Class<? extends F> factoryClass,
                                                Function<F, T> createFunction) {
        Objects.requireNonNull(factoryClass, "factoryClass");
        Objects.requireNonNull(createFunction, "createFunction");
        return injector -> createFunction.apply(injector.getInstance(factoryClass));
    }

    T getInstance(Injector injector);
}