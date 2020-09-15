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

package com.adgadev.jplusone.asserts.impl.rule.exclusion;

import com.adgadev.jplusone.core.registry.LazyInitialisation;
import com.adgadev.jplusone.core.registry.LazyInitialisation.LazyInitialisationType;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

import static com.adgadev.jplusone.asserts.impl.rule.exclusion.ValueHolder.of;
import static com.adgadev.jplusone.core.registry.LazyInitialisation.LazyInitialisationType.COLLECTION;
import static com.adgadev.jplusone.core.registry.LazyInitialisation.LazyInitialisationType.ENTITY;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class LazyInitialisationCriteria {

    private final ValueHolder<String> entityClassName;

    private final ValueHolder<String> fieldName;

    private final LazyInitialisationType type;

    public static LazyInitialisationCriteria loadingEntityCriteria(Class<?> loadedEntityClass) {
        return new LazyInitialisationCriteria(of(loadedEntityClass.getCanonicalName()), of(null), ENTITY);
    }

    public static LazyInitialisationCriteria loadingAnyEntityCriteria() {
        return new LazyInitialisationCriteria(null, of(null), ENTITY);
    }

    public static LazyInitialisationCriteria loadingCollectionCriteria(Class<?> associationOwnerClass, String associationField) {
        return new LazyInitialisationCriteria(of(associationOwnerClass.getCanonicalName()), of(associationField), COLLECTION);
    }

    public static LazyInitialisationCriteria loadingAnyCollectionInEntityCriteria(Class<?> associationOwnerClass) {
        return new LazyInitialisationCriteria(of(associationOwnerClass.getCanonicalName()), null, COLLECTION);
    }

    public static LazyInitialisationCriteria loadingAnyCollectionCriteria() {
        return new LazyInitialisationCriteria(null, null, COLLECTION);
    }

    public boolean matches(LazyInitialisation lazyInitialisation) {
        return lazyInitialisation != null
                && type == lazyInitialisation.getType()
                && (entityClassName == null || Objects.equals(entityClassName.getValue(), lazyInitialisation.getEntityClassName()))
                && (fieldName == null || Objects.equals(fieldName.getValue(), lazyInitialisation.getFieldName()));
    }
}
