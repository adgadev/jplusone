package com.grexdev.nplusone.core.registry;

import lombok.Value;

@Value
public class LazyInitialisation {

    private String entityClassName;

    private String fieldName;

    @Override
    public String toString() {
        return entityClassName + '.' + fieldName + " [LAZY COLLECTION]";
    }
}
