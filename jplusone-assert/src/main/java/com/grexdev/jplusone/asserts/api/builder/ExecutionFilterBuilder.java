package com.grexdev.jplusone.asserts.api.builder;

public interface ExecutionFilterBuilder extends ShouldSectionBuilder {

    ShouldSectionBuilder insideExecutionOfMethod(Class<?> clazz, String methodName);

    ShouldSectionBuilder insideExecutionOfAnyMethodIn(Class<?>... clazz);

}
