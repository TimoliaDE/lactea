package de.timolia.lactea.bukkitloader;

import de.timolia.lactea.bukkitloader.inject.BukkitModule;
import de.timolia.lactea.loader.internal.DefaultRuntime;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * @author David (_Esel)
 */
public class ModuleLoaderPlugin extends JavaPlugin {
    private final DefaultRuntime runtime = DefaultRuntime.create(new File("lactea"));

    @Override
    public void onLoad() {
        runtime.getStartUpController().addGlobalModule(new BukkitModule(this));
        runtime.initialize();
    }

    @Override
    public void onEnable() {
        runtime.enable();
    }

    @Override
    public void onDisable() {
        runtime.shutdown();
    }
}
