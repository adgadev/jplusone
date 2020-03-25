package com.grexdev.nplusone.core.utils;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Slf4j
public class ReflectionUtils {

    private static final Object NO_RESULT = new Object();

    public static Optional<Field> findObjectFieldByFieldValue(Object object, Object searchedFieldValue) {
        return Stream.iterate((Class) object.getClass(), Class::getSuperclass)
                .filter(not(Object.class::equals))
                .map(Class::getDeclaredFields)
                .flatMap(Arrays::stream)
                .filter(field -> field.getType().isAssignableFrom(searchedFieldValue.getClass())) // heuristic
                .filter(field -> getFieldValue(object, field) == searchedFieldValue)
                .findFirst();
    }

    // provided by JRE from java 11, whereas nplusone requires java 9 or higher
    public static <T> Predicate<T> not(Predicate<T> predicate) { return object -> !predicate.test(object); }

    private static Object getFieldValue(Object object, Field field) {
        try {
            field.setAccessible(true);
            return field.get(object);
        } catch (IllegalAccessException e) {
            log.warn("Unable to resolve value of field {} in object {}", field, object);
            return NO_RESULT;
        }
    }


}
