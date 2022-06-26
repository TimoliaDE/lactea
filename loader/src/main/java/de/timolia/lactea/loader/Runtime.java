package de.timolia.lactea.loader;

/**
 * @author David (_Esel)
 */
public interface Runtime {
    <MT> MT getModule(String var1);

    boolean isModuleLoaded(String var1);
}
