package de.timolia.lactea.nukkitloader;

import cn.nukkit.plugin.PluginBase;
import de.timolia.lactea.Bootstrap;
import de.timolia.lactea.core.Lactea;
import de.timolia.lactea.core.lifecycle.startup.LoadEnableSplitStartup;
import de.timolia.lactea.path.SimpleFileRootPathResolver;
import de.timolia.lactea.source.UrlClassPathInjector;
import java.io.File;
import java.net.URLClassLoader;

public class ModuleLoaderPlugin extends PluginBase {
    private LoadEnableSplitStartup startup;

    @Override
    public void onLoad() {
        Bootstrap bootstrap = Bootstrap.build(
            new UrlClassPathInjector((URLClassLoader) getClass().getClassLoader()),
            new SimpleFileRootPathResolver(new File("lactea"))
        );
        bootstrap.initialize();
        postBootstrap(bootstrap);
    }

    private void postBootstrap(Bootstrap bootstrap) {
        Lactea lactea = new Lactea(bootstrap);
        startup = lactea.loadEnableSplit();
        startup.setupDependencyInjection();
    }

    @Override
    public void onEnable() {
        LoadEnableSplitStartup startup = this.startup;
        if (startup == null) {
            getLogger().critical("Startup is not initialized");
            return;
        }
        startup.enableModules();
    }

    @Override
    public void onDisable() {

    }
}
