package de.timolia.lactea.loader.module.definition;

import java.lang.annotation.Target;

@Target({})
public @interface Dependency {
    String value();

    boolean required() default true;
}
