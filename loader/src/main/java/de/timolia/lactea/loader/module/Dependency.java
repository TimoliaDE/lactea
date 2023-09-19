package de.timolia.lactea.loader.module;

public @interface Dependency {
    String value();

    boolean required() default true;
}
