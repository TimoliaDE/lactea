package de.timolia.lactea.loader.module.definition;

public @interface Dependency {
    String value();

    boolean required() default true;
}
