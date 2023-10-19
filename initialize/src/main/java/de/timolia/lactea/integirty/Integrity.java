package de.timolia.lactea.integirty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Integrity {
    private final List<IntegrityCheck> checks = new ArrayList<>();
    private boolean checked;

    public synchronized void register(IntegrityCheck check) {
        Objects.requireNonNull(check, "check");
        checks.add(check);
    }

    public synchronized void checkIntegrity() {
        if (checked) {
            throw new IllegalStateException("Integrity already started");
        }
        for (IntegrityCheck check : checks) {
            try {
                check.ensure();
            } catch (Throwable throwable) {
                throw new IntegrityError("Fatal miss configuration", throwable);
            }
        }
        checked = true;
    }

    static class IntegrityError extends Error {
        public IntegrityError(String message, Throwable throwable) {
            super(message, throwable);
        }
    }
}
