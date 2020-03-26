package com.grexdev.nplusone.test.domain;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

public class MappingUtils {

    public static <T, U> List<U> mapToList(Collection<T> data, Function<T, U> mapper) {
        return data.stream()
                .map(mapper)
                .collect(toList());
    }

    public static <T, U> U mapToItem(T data, Function<T, U> mapper) {
        return mapper.apply(data);
    }

}
