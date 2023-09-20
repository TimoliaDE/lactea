package de.timolia.lactea.loader.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.File;
import java.io.IOException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author David (_Esel)
 */
@RequiredArgsConstructor
public abstract class ConfigFormat {
    public static ConfigFormat YAML = new ConfigFormat("yml", "yaml") {
        @Override
        public ObjectMapper createObjectMapper() {
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            mapper.findAndRegisterModules();
            return mapper;
        }
    };

    public static ConfigFormat JSON = new ConfigFormat("json", "json") {
        @Override
        public ObjectMapper createObjectMapper() {
            ObjectMapper mapper = new ObjectMapper();
            mapper.findAndRegisterModules();
            return mapper;
        }
    };

    @Getter
    private final String extension;
    @Getter
    private final String name;
    private volatile ObjectMapper objectMapper;

    protected abstract ObjectMapper createObjectMapper();

    ObjectMapper objectMapper() {
        if (objectMapper == null) {
            synchronized (this) {
                if (objectMapper == null) {
                    objectMapper = createObjectMapper();
                }
            }
        }
        return objectMapper;
    }

    public <T> T loadConfig(File file, Class<T> clazz) throws IOException {
        return objectMapper().readValue(file, clazz);
    }
}
