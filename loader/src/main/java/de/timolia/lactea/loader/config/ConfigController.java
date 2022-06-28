package de.timolia.lactea.loader.config;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Singleton;
import lombok.SneakyThrows;

/**
 * @author David (_Esel)
 */
@Singleton
public class ConfigController {
    private final Map<String, String> pathPlaceholders = new HashMap<>();
    private final Map<String, ConfigFormat> extensionToFormat = new HashMap<>();
    private final ConfigFormat defaultFormat = ConfigFormat.YAML;

    {
        registerFormat(ConfigFormat.YAML);
        registerPlaceholder("lactea:", "lactea/config/");
    }

    private void registerPlaceholder(String name, String replacement) {
        pathPlaceholders.put(name, replacement);
    }

    private void registerFormat(ConfigFormat format) {
        extensionToFormat.put(format.getExtension(), format);
    }

    private String acceptPlaceholders(String name) {
        for (Map.Entry<String, String> entry : pathPlaceholders.entrySet()) {
            name = name.replaceAll(entry.getKey(), entry.getValue());
        }
        return name;
    }

    public <T> T loadConfig(String name, Class<T> clazz) throws IOException {
        name = acceptPlaceholders(name);
        File file = new File(name);
        File parent = file.getAbsoluteFile().getParentFile();
        parent.mkdirs();
        for (File candidate : parent.listFiles()) {
            if (candidate.isDirectory()) {
                continue;
            }
            String candidateName = candidate.getName();
            int dot = candidateName.lastIndexOf('.');
            if (dot == -1) {
                continue;
            }
            if (candidateName.substring(0, dot).equals(file.getName())) {
                return extensionToFormat.getOrDefault(candidateName.substring(dot + 1), defaultFormat).loadConfig(candidate, clazz);
            }
        }
        return createDefault(parent, file.getName(), clazz);
    }

    @SneakyThrows
    private <T> T createDefault(File parent, String name, Class<T> clazz) throws IOException {
        ConfigFormat format = defaultFormat;
        File file = new File(parent, name + '.' + format.getExtension());
        format.objectMapper().writeValue(file, clazz.newInstance());
        file.createNewFile();
        return format.loadConfig(file, clazz);
    }
}
