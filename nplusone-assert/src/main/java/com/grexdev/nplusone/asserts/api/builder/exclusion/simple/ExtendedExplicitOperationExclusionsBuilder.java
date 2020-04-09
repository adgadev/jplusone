package com.grexdev.nplusone.asserts.api.builder.exclusion.simple;

import com.grexdev.nplusone.asserts.api.builder.ConditionDoneBuilder;
import com.grexdev.nplusone.asserts.api.builder.exclusion.complex.ExplicitOperationExclusionBuilder;

import java.util.function.Function;

public interface ExtendedExplicitOperationExclusionsBuilder extends SimpleExplicitOperationExclusionsBuilder {

    ConditionDoneBuilder exceptAnyOf(Function<ExplicitOperationExclusionBuilder, ExplicitOperationExclusionBuilder> exclusions);

    ConditionDoneBuilder exceptAllOf(Function<ExplicitOperationExclusionBuilder, ExplicitOperationExclusionBuilder> exclusions);

    ConditionDoneBuilder exceptAllOfInOrder(Function<ExplicitOperationExclusionBuilder, ExplicitOperationExclusionBuilder> exclusions);

}
