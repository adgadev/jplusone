package com.grexdev.jplusone.core.registry;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LazyInitialisation {

    enum LazyInitialisationType {ENTITY, COLLECTION}

    private final String entityClassName;

    private final String fieldName;

    private final LazyInitialisationType type;

    static LazyInitialisation entityLazyInitialisation(String entityClassName) {
        return new LazyInitialisation(entityClassName, null, LazyInitialisationType.ENTITY);
    }

    public static LazyInitialisation collectionLazyInitialisation(String entityClassName, String fieldName) {
        return new LazyInitialisation(entityClassName, fieldName, LazyInitialisationType.COLLECTION);
    }

    @Override
    public String toString() {
        return type == LazyInitialisationType.COLLECTION
                ? entityClassName + '.' + fieldName + " [FETCHING COLLECTION]"
                : entityClassName + " [FETCHING ENTITY]";
    }

}
