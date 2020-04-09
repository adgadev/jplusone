package com.grexdev.nplusone.asserts.api.builder.exclusion.simple;

import com.grexdev.nplusone.asserts.api.builder.ConditionDoneBuilder;
import com.grexdev.nplusone.asserts.api.builder.exclusion.complex.ImplicitOperationExclusionBuilder;

import java.util.function.Function;

public interface ExtendedImplicitOperationExclusionsBuilder extends SimpleImplicitOperationExclusionsBuilder {

    ConditionDoneBuilder exceptAnyOf(Function<ImplicitOperationExclusionBuilder, ImplicitOperationExclusionBuilder> exclusions);

    ConditionDoneBuilder exceptAllOf(Function<ImplicitOperationExclusionBuilder, ImplicitOperationExclusionBuilder> exclusions);

    ConditionDoneBuilder exceptAllOfInOrder(Function<ImplicitOperationExclusionBuilder, ImplicitOperationExclusionBuilder> exclusions);

}
