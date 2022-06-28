package de.timolia.lactea.translate;

import de.timolia.lactea.loader.config.ConfigDefinition;
import lombok.Getter;
import lombok.Setter;

/**
 * @author David (_Esel)
 */
@Getter
@Setter
@ConfigDefinition("lactea:hello")
public class TestConfig {
    private int a = 2;
    private int b = 5;

    @Override
    public String toString() {
        return "TestConfig{" +
            "a=" + a +
            ", b=" + b +
            '}';
    }
}
