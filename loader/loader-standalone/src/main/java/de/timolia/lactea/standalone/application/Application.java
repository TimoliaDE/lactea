package de.timolia.lactea.standalone.application;

import de.timolia.lactea.Bootstrap;
import de.timolia.lactea.core.Lactea;
import de.timolia.lactea.path.SimpleFileRootPathResolver;
import de.timolia.lactea.source.UrlClassPathInjector;
import java.io.File;
import java.net.URLClassLoader;

public class Application {
    public void boot(File folder) {
        Bootstrap bootstrap = Bootstrap.build(
            new UrlClassPathInjector((URLClassLoader) getClass().getClassLoader()),
            new SimpleFileRootPathResolver(folder)
        );
        bootstrap.initialize();
        postBootstrap(bootstrap);
    }

    private void postBootstrap(Bootstrap bootstrap) {
        Lactea lactea = new Lactea(bootstrap);
        lactea.singleEntrypoint().performStartup();
    }
}
