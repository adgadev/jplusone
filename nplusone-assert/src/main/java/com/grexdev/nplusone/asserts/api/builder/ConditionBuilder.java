package com.grexdev.nplusone.asserts.api.builder;

import com.grexdev.nplusone.asserts.api.builder.exclusion.simple.ExtendedExplicitOperationExclusionsBuilder;
import com.grexdev.nplusone.asserts.api.builder.exclusion.simple.ExtendedImplicitOperationExclusionsBuilder;

public interface ConditionBuilder extends LimitingAmount<SimpleConditionBuilder> {

    ExtendedImplicitOperationExclusionsBuilder noImplicitOperations();

    ExtendedExplicitOperationExclusionsBuilder noExplicitOperations();

}
