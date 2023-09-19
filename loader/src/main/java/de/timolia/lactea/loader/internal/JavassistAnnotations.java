package de.timolia.lactea.loader.internal;

import de.timolia.lactea.loader.module.ModuleDescription.DependencyDescription;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.AnnotationMemberValue;
import javassist.bytecode.annotation.ArrayMemberValue;
import javassist.bytecode.annotation.BooleanMemberValue;
import javassist.bytecode.annotation.MemberValue;
import javassist.bytecode.annotation.StringMemberValue;

/**
 * @author David (_Esel)
 */
public class JavassistAnnotations {
    public static String stringValue(Annotation annotation, String name) {
        StringMemberValue value = (StringMemberValue) annotation.getMemberValue(name);
        return value.getValue();
    }

    public static DependencyDescription[] dependencyDescriptions(Annotation annotation, String name) {
        ArrayMemberValue value = (ArrayMemberValue) annotation.getMemberValue(name);
        MemberValue[] dependencies = value.getValue();
        DependencyDescription[] dependencyDescriptions = new DependencyDescription[dependencies.length];
        for (int i = 0; i < dependencies.length; i++) {
            Annotation dependencyAnnotation = ((AnnotationMemberValue) dependencies[i]).getValue();
            dependencyDescriptions[i] = new DependencyDescription(
                stringValue(dependencyAnnotation, "value"),
                ((BooleanMemberValue) dependencyAnnotation.getMemberValue("required")).getValue()
            );
        }
        return dependencyDescriptions;
    }
}
