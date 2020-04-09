package com.grexdev.nplusone.asserts.api.builder.exclusion.simple;

import com.grexdev.nplusone.asserts.api.builder.ConditionDoneBuilder;

public interface SimpleImplicitOperationExclusionsBuilder extends ConditionDoneBuilder {

    ConditionDoneBuilder exceptLoadingEntity(Class<?> loadedEntityClass);

    ConditionDoneBuilder exceptLoadingAnyEntity();

    ConditionDoneBuilder exceptLoadingCollection(Class<?> associationOwnerClass, String associationField);

    ConditionDoneBuilder exceptLoadingAnyCollection();

    ConditionDoneBuilder exceptFlushingData();

}
