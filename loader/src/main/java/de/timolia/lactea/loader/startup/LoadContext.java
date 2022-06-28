package de.timolia.lactea.loader.startup;

import com.google.inject.Module;

/**
 * @author David (_Esel)
 */
public interface LoadContext {
    void installGlobalModule(Module module);

    void installModule(Module module);

    void installModule(Class<? extends Module> moduleClass);
}
