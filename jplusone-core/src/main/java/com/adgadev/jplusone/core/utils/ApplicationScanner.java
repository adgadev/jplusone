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

package com.adgadev.jplusone.core.utils;

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
