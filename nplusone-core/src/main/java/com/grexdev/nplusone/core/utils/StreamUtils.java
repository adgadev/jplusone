package com.grexdev.nplusone.core.utils;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Function;
import java.util.function.Predicate;

public class StreamUtils {

    public static <T> Function<T, Tupple<T>> currentAndPreviousElements() {
        Holder<T> previousElementHolder = new Holder<>();

        return element -> {
            Tupple<T> result = new Tupple<>(previousElementHolder.getValue(), element);
            previousElementHolder.setValue(element);
            return result;
        };
    }

    public static <T> Predicate<Tupple<T>> anyElementMatches(Predicate<T> predicate) {
        return window -> window.previousElement != null && predicate.test(window.previousElement)
                || window.currentElement != null && predicate.test(window.currentElement);
    }

    public static <T> Predicate<Tupple<T>> everyElementMatches(Predicate<T> predicate) {
        return window -> window.previousElement != null && predicate.test(window.previousElement)
                && window.currentElement != null && predicate.test(window.currentElement);
    }

    @Getter
    @RequiredArgsConstructor
    public static class Tupple<T> {

        private final T previousElement;

        private final T currentElement;

        public T getMainElement() {
            return currentElement;
        }
    }

    @Data
    private static class Holder<T> {
        private T value;
    }
}
