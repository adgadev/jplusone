package com.grexdev.nplusone.asserts.api.builder.exclusion.simple;

import com.grexdev.nplusone.asserts.api.builder.ConditionDoneBuilder;

public interface SimpleExplicitOperationExclusionsBuilder extends ConditionDoneBuilder {

    ConditionDoneBuilder exceptFetchingData();

    ConditionDoneBuilder exceptFetchingDataVia(Class<?> clazz, String methodName);

    ConditionDoneBuilder exceptFetchingDataVia(String className, String methodName);

    ConditionDoneBuilder exceptModifyingData();

    ConditionDoneBuilder exceptModifyingDataVia(Class<?> clazz, String methodName);

    ConditionDoneBuilder exceptModifyingDataVia(String className, String methodName);

}
