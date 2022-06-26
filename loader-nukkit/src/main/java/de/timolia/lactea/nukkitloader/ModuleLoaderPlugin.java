package de.timolia.lactea.nukkitloader;

import cn.nukkit.plugin.PluginBase;
import de.timolia.lactea.loader.module.ModuleManager;
import java.io.File;

/**
 * @author David (_Esel)
 */
public class ModuleLoaderPlugin extends PluginBase {
    private final ModuleManager moduleManager = new ModuleManager(new File("lactea"));

    @Override
    public void onLoad() {
        moduleManager.loadLibraries();
        moduleManager.scan();
        moduleManager.loadAll();
    }

    @Override
    public void onEnable() {
        moduleManager.enableAll();
    }

    @Override
    public void onDisable() {
        moduleManager.disableAll();
    }
}
