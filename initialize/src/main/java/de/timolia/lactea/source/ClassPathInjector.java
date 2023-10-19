package de.timolia.lactea.source;

import java.net.URI;

public interface ClassPathInjector {
    void addToClassPath(URI file);

    void addToClassPath(SourceRoot sourceRoot);
}
