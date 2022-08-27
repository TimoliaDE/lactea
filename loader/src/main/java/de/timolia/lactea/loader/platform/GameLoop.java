package de.timolia.lactea.loader.platform;

import java.time.temporal.TemporalUnit;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

public interface GameLoop {
    CompletableFuture<?> runTimer(Runnable task, long delay, long period, TemporalUnit unit);

    CompletableFuture<?> runLater(Runnable task, long delay, TemporalUnit unit);

    <V> CompletableFuture<V> runTimer(Callable<V> task, long delay, long period, TemporalUnit unit);

    <V> CompletableFuture<V> runLater(Callable<V> task, long delay, TemporalUnit unit);
}
