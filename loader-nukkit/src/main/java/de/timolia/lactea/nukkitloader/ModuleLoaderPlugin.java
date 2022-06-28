package de.timolia.lactea.nukkitloader;

import cn.nukkit.plugin.PluginBase;
import de.timolia.lactea.loader.internal.DefaultRuntime;
import de.timolia.lactea.loader.module.ModuleManager;
import de.timolia.lactea.loader.startup.StartUpController;
import java.io.File;

/**
 * @author David (_Esel)
 */
public class ModuleLoaderPlugin extends PluginBase {
    private final ModuleManager moduleManager = new ModuleManager(new File("lactea"));
    private final DefaultRuntime runtime = new DefaultRuntime(moduleManager, new StartUpController());

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
        moduleManager.disableAll();
    }
}
