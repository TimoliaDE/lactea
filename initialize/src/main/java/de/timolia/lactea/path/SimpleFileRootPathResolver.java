package de.timolia.lactea.path;

import de.timolia.lactea.source.DirectorySourceRoot;
import de.timolia.lactea.source.SourceRoot;
import java.io.File;

public class SimpleFileRootPathResolver implements PathResolver {
    private final File root;

    public SimpleFileRootPathResolver(File root) {
        this.root = root;
    }

    @Override
    public SourceRoot resolve(PathKey key) {
        return new DirectorySourceRoot(new File(root, key.name()));
    }

    @Override
    public SourceRoot resolveOptional(PathKey key) {
        return resolve(key);
    }
}
