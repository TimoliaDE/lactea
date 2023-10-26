package de.timolia.lactea.source;

import java.net.URI;
import java.util.function.Consumer;

public interface SourceRoot {
    void forEachJar(SourceElementAcceptor consumer);

    interface SourceElementAcceptor {
        void accept(URI uri);
    }
}
