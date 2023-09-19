package de.timolia.lactea.loader.inject;

public interface InstanceRegistry<I> {
    void register(I instance);
    void unregister(I instance);
}
