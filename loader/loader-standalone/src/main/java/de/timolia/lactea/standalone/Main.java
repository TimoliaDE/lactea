package de.timolia.lactea.standalone;

import de.timolia.lactea.standalone.application.Application;
import java.io.File;

public class Main {
    public static void main(String[] args) {
        File folder = new File("lactea");
        Application application = new Application();
        application.boot(folder);
    }
}
