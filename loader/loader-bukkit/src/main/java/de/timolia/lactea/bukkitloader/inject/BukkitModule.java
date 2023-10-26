package de.timolia.lactea.bukkitloader.inject;

import com.google.inject.Binder;
import com.google.inject.Module;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.messaging.Messenger;

public class BukkitModule implements Module {
    private final Plugin plugin;

    public BukkitModule(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void configure(Binder binder) {
        binder.bind(Plugin.class).toInstance(plugin);
        configureSingletons(binder);
    }

    private void configureSingletons(Binder binder) {
        binder.bind(PluginManager.class).toInstance(Bukkit.getPluginManager());
        binder.bind(ConsoleCommandSender.class).toInstance(Bukkit.getConsoleSender());
        binder.bind(Server.class).toInstance(Bukkit.getServer());
        binder.bind(Messenger.class).toInstance(Bukkit.getMessenger());
    }
}
