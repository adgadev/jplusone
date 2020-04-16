/*
 * Copyright (c) 2020 Adam Gaj
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.grexdev.jplusone.core.utils;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

public class StreamUtils {

    public static <T> Collector<T, ?, List<T>> toListReversed() {
        return collectingAndThen(toList(), list -> {
            Collections.reverse(list);
            return list;
        });
    }

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

    public static <T> List<T> filterToList(Collection<T> list, Predicate<T> predicate) {
        return list.stream()
                .filter(predicate::test)
                .collect(toList());
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
