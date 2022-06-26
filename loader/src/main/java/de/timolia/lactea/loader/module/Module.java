package de.timolia.lactea.loader.module;

import java.io.File;
import java.util.logging.Logger;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * @author David (_Esel)
 */
@Getter
public class Module {
    @Setter(AccessLevel.PACKAGE)
    private ModuleDescription description;
    private final Logger logger = Logger.getLogger(getClass().getName());

    public void onEnable() throws Exception {
    }

    public void onLoad() throws Exception {
    }

    public void onDisable() throws Exception {

    }

    public void onPostDisable() throws Exception {
    }

    public File getConfigFolder() {
        return new File("configuration");
    }
}
