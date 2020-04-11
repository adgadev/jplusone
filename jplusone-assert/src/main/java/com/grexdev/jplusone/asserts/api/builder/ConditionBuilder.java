package com.grexdev.jplusone.asserts.api.builder;

import com.grexdev.jplusone.asserts.api.builder.exclusion.simple.ExtendedExplicitOperationExclusionsBuilder;
import com.grexdev.jplusone.asserts.api.builder.exclusion.simple.ExtendedImplicitOperationExclusionsBuilder;

public interface ConditionBuilder extends LimitingAmount<SimpleConditionBuilder> {

    ExtendedImplicitOperationExclusionsBuilder noImplicitOperations();

    ExtendedExplicitOperationExclusionsBuilder noExplicitOperations();

}
