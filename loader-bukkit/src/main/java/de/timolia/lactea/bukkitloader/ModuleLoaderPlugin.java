package de.timolia.lactea.bukkitloader;

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
