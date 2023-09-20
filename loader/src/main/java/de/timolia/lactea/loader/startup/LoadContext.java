package de.timolia.lactea.loader.startup;

import com.google.inject.Module;
import de.timolia.lactea.loader.inject.InjectedInstance;

/**
 * @author David (_Esel)
 */
public interface LoadContext {
    void installGlobalModule(Module module);

    void installModule(InjectedInstance<Module> module);
}
