package de.timolia.lactea.loader.startup.internal;

public final class Integrity {
    private interface IntegrityCheck {
        void ensure();
    }

    private static IntegrityCheck ensureClassAvailable(String className, String moduleLib) {
        return () -> {
            try {
                Class.forName(className);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(
                    "Module " + moduleLib + " is not present on classpath"
                    + " make sure it is present in the libs directory",
                    e);
            }
        };
    }

    private static class IntegrityError extends Error {
        public IntegrityError(String message, Throwable throwable) {
            super(message, throwable);
        }
    }

    public static void checkIntegrity() {
        IntegrityCheck[] checks = new IntegrityCheck[] {
            ensureClassAvailable("com.google.inject.Module", "Guice")
        };
        for (IntegrityCheck check : checks) {
            try {
                check.ensure();
            } catch (Throwable throwable) {
                throw new IntegrityError("Fatal miss configuration", throwable);
            }
        }
    }
}
