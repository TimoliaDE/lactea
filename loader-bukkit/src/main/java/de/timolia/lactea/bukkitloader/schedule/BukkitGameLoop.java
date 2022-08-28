package de.timolia.lactea.bukkitloader.schedule;

import de.timolia.lactea.loader.platform.GameLoop;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.temporal.TemporalUnit;
import java.util.concurrent.*;

@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class BukkitGameLoop implements GameLoop {
    private final BukkitScheduler scheduler;
    private final Plugin plugin;
    private final Server server;

    @Override
    public CompletableFuture<?> runTimer(Runnable task, long delay, long period, TemporalUnit unit) {
        BukkitFuture<?> future = new BukkitFuture<>();
        runTimer(future, future.completeAfter(task), delay, period, unit);
        return future;
    }

    @Override
    public CompletableFuture<?> runLater(Runnable task, long delay, TemporalUnit unit) {
        BukkitFuture<?> future = new BukkitFuture<>();
        runLater(future, future.completeAfter(task), delay, unit);
        return future;
    }

    @Override
    public <V> CompletableFuture<V> runTimer(Callable<V> task, long delay, long period, TemporalUnit unit) {
        BukkitFuture<V> future = new BukkitFuture<>();
        runTimer(future, future.completeAfter(task), delay, period, unit);
        return future;
    }

    @Override
    public <V> CompletableFuture<V> runLater(Callable<V> task, long delay, TemporalUnit unit) {
        BukkitFuture<V> future = new BukkitFuture<>();
        runLater(future, future.completeAfter(task), delay, unit);
        return future;
    }

    @Override
    public boolean isOnGameThread() {
        return server.isPrimaryThread();
    }

    @Override
    public void ensureOnGameThread() {
        if (!isOnGameThread()) {
            throw new IllegalStateException("Outside of eventloop");
        }
    }

    @Override
    public CompletableFuture<?> runInGameThread(Runnable runnable) {
        BukkitFuture<?> future = new BukkitFuture<>();
        Runnable wrapped = future.completeAfter(runnable);
        if (isOnGameThread()) {
            wrapped.run();
        } else {
            runLater(future, wrapped, 1, TickUnit.GAME);
        }
        return future;
    }

    private void runLater(BukkitFuture<?> future, Runnable wrapped,
                          long delay, TemporalUnit unit) {
        long delayTicks = toTicks(delay, unit);
        future.setTask(scheduler.runTaskLater(plugin, wrapped, delayTicks));
    }

    private void runTimer(BukkitFuture<?> future, Runnable wrapped,
                          long delay, long period, TemporalUnit unit) {
        long delayTicks = toTicks(delay, unit);
        long periodTicks = toTicks(period, unit);
        future.setTask(scheduler.runTaskTimer(plugin, wrapped, delayTicks, periodTicks));
    }

    private static long toTicks(long time, TemporalUnit unit) {
        if (unit == TickUnit.GAME) {
            return time;
        }
        return unit.getDuration().toMillis() / TickUnit.GAME.ticksPerSecond();
    }
}
