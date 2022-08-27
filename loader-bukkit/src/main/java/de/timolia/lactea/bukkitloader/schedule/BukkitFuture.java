package de.timolia.lactea.bukkitloader.schedule;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class BukkitFuture<T> extends CompletableFuture<T> {
    @Getter
    @Setter(AccessLevel.PACKAGE)
    private BukkitTask task;
    private final AtomicBoolean cancelled = new AtomicBoolean();

    Runnable completeAfter(Runnable runnable) {
        return () -> {
            try {
                runnable.run();
                super.complete(null);
            } catch (Throwable throwable) {
                super.completeExceptionally(throwable);
            }
        };
    }

    Runnable completeAfter(Callable<T> callable) {
        return () -> {
            try {
                super.complete(callable.call());
            } catch (Throwable throwable) {
                super.completeExceptionally(throwable);
            }
        };
    }

    @Override
    public boolean complete(T value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean completeExceptionally(Throwable ex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        if (mayInterruptIfRunning) {
            throw new UnsupportedOperationException("mayInterruptIfRunning is not supported");
        }
        if (cancelled.compareAndSet(false, true)) {
            task.cancel();
            return true;
        }
        return false;
    }

    @Override
    public boolean isCancelled() {
        return task.isCancelled();
    }
}
