package com.grexdev.jplusone.asserts.api.builder.exclusion.simple;

import com.grexdev.jplusone.asserts.api.builder.exclusion.complex.ExplicitOperationExclusionBuilder;
import com.grexdev.jplusone.asserts.api.builder.ConditionDoneBuilder;

import java.util.function.Function;

public interface ExtendedExplicitOperationExclusionsBuilder extends SimpleExplicitOperationExclusionsBuilder {

    ConditionDoneBuilder exceptAnyOf(Function<ExplicitOperationExclusionBuilder, ExplicitOperationExclusionBuilder> exclusions);

    ConditionDoneBuilder exceptAllOf(Function<ExplicitOperationExclusionBuilder, ExplicitOperationExclusionBuilder> exclusions);

    ConditionDoneBuilder exceptAllOfInOrder(Function<ExplicitOperationExclusionBuilder, ExplicitOperationExclusionBuilder> exclusions);

}
