package de.timolia.lactea.bukkitloader;

import de.timolia.lactea.Bootstrap;
import de.timolia.lactea.bukkitloader.inject.BukkitModule;
import de.timolia.lactea.core.Lactea;
import de.timolia.lactea.core.lifecycle.startup.LoadEnableSplitStartup;
import de.timolia.lactea.path.SimpleFileRootPathResolver;
import de.timolia.lactea.source.UrlClassPathInjector;
import java.net.URLClassLoader;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class ModuleLoaderPlugin extends JavaPlugin {
    private LoadEnableSplitStartup startup;

    @Override
    public void onLoad() {
        Bootstrap bootstrap = Bootstrap.build(
            new UrlClassPathInjector((URLClassLoader) getClassLoader()),
            new SimpleFileRootPathResolver(new File("lactea"))
        );
        bootstrap.initialize();
        postBootstrap(bootstrap);
    }

    private void postBootstrap(Bootstrap bootstrap) {
        Lactea lactea = new Lactea(bootstrap, new BukkitModule(this));
        startup = lactea.loadEnableSplit();
        startup.setupDependencyInjection();
    }

    @Override
    public void onEnable() {
        LoadEnableSplitStartup startup = this.startup;
        if (startup == null) {
            getLogger().severe("Startup is not initialized");
            return;
        }
        startup.enableModules();
    }

    @Override
    public void onDisable() {

    }
}
