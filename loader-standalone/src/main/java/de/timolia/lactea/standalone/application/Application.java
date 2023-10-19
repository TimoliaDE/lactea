package de.timolia.lactea.standalone.application;

import de.timolia.lactea.loader.internal.DefaultRuntime;
import java.io.File;

public class Application {
    public void boot(File folder) {
        DefaultRuntime runtime = DefaultRuntime.create(folder);
        runtime.loadLibraries();
        runtime.initialize();
        Runtime.getRuntime().addShutdownHook(new Thread(runtime::shutdown, "Lactea shutdown"));
        runtime.enable();
    }
}
