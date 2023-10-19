package de.timolia.lactea.integirty;

public class EnsureClassAvailable implements IntegrityCheck {
    private final String className;
    private final String moduleLib;

    public EnsureClassAvailable(String className, String moduleLib) {
        this.className = className;
        this.moduleLib = moduleLib;
    }

    @Override
    public void ensure() {
        try {
            Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(
                "Module " + moduleLib + " is not present on classpath"
                    + " make sure it is present in the libs directory",
                e);
        }
    }
}
