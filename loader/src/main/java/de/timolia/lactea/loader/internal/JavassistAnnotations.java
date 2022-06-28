package de.timolia.lactea.loader.internal;

import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.StringMemberValue;

/**
 * @author David (_Esel)
 */
public class JavassistAnnotations {
    public static String stringValue(Annotation annotation, String name) {
        StringMemberValue value = (StringMemberValue) annotation.getMemberValue(name);
        return value.getValue();
    }
}
