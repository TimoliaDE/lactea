package de.timolia.lactea;

import de.timolia.lactea.integirty.EnsureClassAvailable;
import de.timolia.lactea.integirty.Integrity;
import de.timolia.lactea.integirty.IntegrityCheck;
import de.timolia.lactea.path.LacteaPathKeys;
import de.timolia.lactea.path.PathResolver;
import de.timolia.lactea.source.ClassPathInjector;
import de.timolia.lactea.source.SourceRoot;

public class Bootstrap {
    private final Integrity integrity = new Integrity();
    private final ClassPathInjector classPathInjector;
    private final PathResolver pathResolver;

    private Bootstrap(
        ClassPathInjector classPathInjector,
        PathResolver pathResolver
    ) {
        this.classPathInjector = classPathInjector;
        this.pathResolver = pathResolver;
    }

    public static Bootstrap build(
        ClassPathInjector classPathInjector,
        PathResolver pathResolver
    ) {
        Bootstrap bootstrap = new Bootstrap(classPathInjector, pathResolver);
        bootstrap.registerIntegrityCheck(
            new EnsureClassAvailable("com.google.inject.Module", "Guice")
        );
        return bootstrap;
    }

    public void registerIntegrityCheck(IntegrityCheck integrityCheck) {
        integrity.register(integrityCheck);
    }

    public void initialize() {
        SourceRoot librariesRoot = pathResolver.resolve(LacteaPathKeys.LIBRARIES);
        classPathInjector.addToClassPath(librariesRoot);
        integrity.checkIntegrity();
    }
}
