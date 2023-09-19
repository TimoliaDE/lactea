package de.timolia.lactea.loader.module;

import de.timolia.lactea.loader.startup.StartUpController;
import de.timolia.lactea.loader.startup.internal.ModuleLoadContext;
import java.io.File;
import java.lang.reflect.InaccessibleObjectException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.RequiredArgsConstructor;

/**
 * @author David (_Esel)
 */
@RequiredArgsConstructor
public class ModuleManager {
    private final Logger logger = Logger.getLogger(ModuleManager.class.getName());
    private final File container;
    private final Map<String, ModuleDescription> definitions = new HashMap<>();
    private final Map<String, LacteaModule> modules = new HashMap<>();
    private final Set<String> loaded = new HashSet<>();

    public boolean isLoaded(String name) {
        return loaded.contains(name);
    }

    public LacteaModule byName(String name) {
        return modules.get(name);
    }

    private boolean checkInvalidCandidate(File candidate) {
        String name = candidate.getName();
        return !candidate.isFile()
            || name.startsWith("!")
            || !name.endsWith(".jar");
    }

    private File childDirectory(String name) {
        File directory = new File(container, name);
        directory.mkdirs();
        return directory;
    }

    private void forEachJar(String namespace, Consumer<File> consumer) {
        File jarDirectory = childDirectory(namespace);
        File[] candidates = jarDirectory.listFiles();
        if (candidates != null) {
            for (File candidate : candidates) {
                if (checkInvalidCandidate(candidate)) {
                    continue;
                }
                consumer.accept(candidate);
            }
        }
    }

    public void loadLibraries() {
        URLClassLoader loader = (URLClassLoader) ModuleManager.class.getClassLoader();
        Method addURL = addUrlMethod();
        forEachJar("libraries", candidate -> {
            try {
                addURL.invoke(loader, candidate.toURI().toURL());
            } catch (IllegalAccessException | InvocationTargetException | MalformedURLException e) {
                logger.log(Level.WARNING, "Failed to load library: " + candidate.getName());
            }
        });
    }

    public void scan() {
        forEachJar("modules", candidate -> {
            try (JarFile jar = new JarFile(candidate)) {
                ModuleDescription desc = new ModuleDescription(candidate, jar);
                if (definitions.put(desc.getName(), desc) != null) {
                    logger.warning("Duplicated definition for " + desc.getName());
                }
            } catch (Throwable throwable) {
                logger.log(Level.SEVERE, "Failed to scan " + candidate, throwable);
            }
        });
    }

    public List<ModuleLoadContext> loadAll(StartUpController startUpController) {
        URLClassLoader loader = (URLClassLoader) ModuleManager.class.getClassLoader();
        Method addURL = addUrlMethod();
        List<ModuleLoadContext> contexts = new ArrayList<>();
        for (ModuleDescription description : definitions.values()) {
            try {
                addURL.invoke(loader, description.url());
                Class<? extends LacteaModule> main = description.mainClass();
                LacteaModule module = main.getConstructor().newInstance();
                module.setDescription(description);
                modules.put(description.getName(), module);
                ModuleLoadContext loadContext = startUpController.loadModule(module);
                contexts.add(loadContext);
                loaded.add(description.getName());
            } catch (Throwable ex) {
                logger.log(Level.SEVERE, "Failed to load " + description.nameAndLocation(), ex);
            }
        }
        return contexts;
    }

    public void enableAll(StartUpController startUpController) {
        for (LacteaModule module : modules.values()) {
            try {
                module.onEnable(startUpController.enableContext(module));
            } catch (Exception ex) {
                logger.log(Level.SEVERE, "Failed to enable " + module, ex);
            }
        }
    }

    public void disableAll() {
        for (LacteaModule module : modules.values()) {
            try {
                module.onDisable();
            } catch (Exception ex) {
                logger.log(Level.SEVERE, "Failed to disable " + module, ex);
            }
        }
        for (LacteaModule module : modules.values()) {
            try {
                module.onPostDisable();
            } catch (Exception ex) {
                logger.log(Level.SEVERE, "Failed to post disable " + module, ex);
            }
        }
    }

    private static Method addUrlMethod() {
        try {
            Method addURL = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            addURL.setAccessible(true);
            return addURL;
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InaccessibleObjectException e) {
            throw new RuntimeException("Make sure to open up reflection", e);
        }
    }
}
