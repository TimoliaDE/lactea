package de.timolia.lactea.path;

import de.timolia.lactea.source.SourceRoot;

public interface PathResolver {
    SourceRoot resolve(PathKey key);

    SourceRoot resolveOptional(PathKey key);
}
