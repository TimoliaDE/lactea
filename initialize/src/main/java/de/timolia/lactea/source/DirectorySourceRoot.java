package de.timolia.lactea.source;

import java.io.File;

public class DirectorySourceRoot implements SourceRoot {
    private final File root;

    public DirectorySourceRoot(File root) {
        this.root = root;
    }

    private void makeDirectory() {
        root.mkdirs();
    }

    private boolean checkInvalidCandidate(File candidate) {
        String name = candidate.getName();
        return !candidate.isFile()
            || name.startsWith("!")
            || !name.endsWith(".jar");
    }

    public static DirectorySourceRoot create(File root) {
        return new DirectorySourceRoot(root);
    }

    @Override
    public void forEachJar(SourceElementAcceptor consumer) {
        makeDirectory();
        File[] candidates = root.listFiles();
        if (candidates == null) {
            return;
        }
        for (File candidate : candidates) {
            if (checkInvalidCandidate(candidate)) {
                continue;
            }
            consumer.accept(candidate.toURI());
        }
    }
}
