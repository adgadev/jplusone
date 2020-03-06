package com.grexdev.nplusone.core.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class ApplicationScanner {

    private final ApplicationContext context;

    public String resolveRootApplicationPackage() {
        return findSpringBootApplicationClass()
            .map(clazz -> clazz.getPackage().getName())
            .orElse(null);
    }

    private Optional<Class<?>> findSpringBootApplicationClass() {
        Map<String, Object> beans = context.getBeansWithAnnotation(SpringBootApplication.class);
        Iterator<Object> iterator = beans.values().iterator();

        return Optional.of(iterator)
                .filter(Iterator::hasNext)
                .map(Iterator::next)
                .map(Object::getClass);
    }
}
