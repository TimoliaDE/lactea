package de.timolia.lactea.loader.module.definition;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author David (_Esel)
 */
@Target({ElementType.TYPE})
public @interface ModuleDefinition {
    String value();

    Dependency[] dependencies() default {};
}
