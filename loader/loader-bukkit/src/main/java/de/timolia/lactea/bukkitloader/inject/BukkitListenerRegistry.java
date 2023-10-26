package de.timolia.lactea.bukkitloader.inject;

import de.timolia.lactea.inject.InstanceRegistry;
import javax.inject.Inject;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class BukkitListenerRegistry implements InstanceRegistry<Listener> {
    private final PluginManager pluginManager;
    private final Plugin plugin;

    @Inject
    public BukkitListenerRegistry(PluginManager pluginManager, Plugin plugin) {
        this.pluginManager = pluginManager;
        this.plugin = plugin;
    }

    @Override
    public void register(Listener instance) {
        pluginManager.registerEvents(instance, plugin);
    }

    @Override
    public void unregister(Listener instance) {
        HandlerList.unregisterAll(instance);
    }
}
