package de.timolia.lactea.loader;

/**
 * @author David (_Esel)
 */
public interface Runtime {
    <MT> MT getModule(String name);

    boolean isModuleLoaded(String name);
}
