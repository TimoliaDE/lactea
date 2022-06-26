package de.timolia.lactea.loader.module;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarFile;
import lombok.RequiredArgsConstructor;

/**
 * @author David (_Esel)
 */
@RequiredArgsConstructor
public class ModuleManager {
    private final File container;
    private final Map<String, ModuleDescription> definitions = new HashMap<>();
    private final Map<String, Module> modules = new HashMap<>();

    private boolean checkInvalidCandidate(File candidate) {
        String name = candidate.getName();
        return !candidate.isFile()
            || name.startsWith("!")
            || !name.endsWith(".jar");
    }

    public void scan() {
        File[] candidates = container.listFiles();
        if (candidates != null) {
            for (File candidate : candidates) {
                if (checkInvalidCandidate(candidate)) {
                    continue;
                }
                try (JarFile jar = new JarFile(candidate)) {
                    ModuleDescription desc = new ModuleDescription(candidate, jar);
                    if (definitions.put(desc.getName(), desc) != null) {
                        //TODO FAIL
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public void loadAll() throws Exception {
        URLClassLoader loader = (URLClassLoader) ModuleManager.class.getClassLoader();
        Method addURL = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
        addURL.setAccessible(true);
        for (ModuleDescription description : definitions.values()) {
            addURL.invoke(loader, description.url());
            Class<?> main = loader.loadClass(description.getMain());
            try {
                Module module = (Module) main.getConstructor().newInstance();
                module.setDescription(description);
                modules.put(description.getName(), module);
                module.onLoad();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }
}
