package de.timolia.lactea.path;

public final class LacteaPathKeys {
    public static PathKey LIBRARIES = new PathKey() {
        @Override
        public String name() {
            return "libraries";
        }
    };
    public static PathKey MODULES = new PathKey() {
        @Override
        public String name() {
            return "modules";
        }
    };
}
