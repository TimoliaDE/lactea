package de.timolia.lactea.bukkitloader.inject;

import de.timolia.lactea.loader.inject.AutoWiredRegistry;
import java.lang.annotation.Annotation;
import javax.inject.Inject;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class BukkitListenerRegistry implements AutoWiredRegistry<Listener> {
    private final PluginManager pluginManager;
    private final Plugin plugin;

    @Override
    public void register(Listener instance) {
        pluginManager.registerEvents(instance, plugin);
    }

    @Override
    public void unregister(Listener instance) {
        HandlerList.unregisterAll(instance);
    }

    @Override
    public Class<? extends Annotation> autoWireAnnotation() {
        return ListenerAutoWire.class;
    }
}
