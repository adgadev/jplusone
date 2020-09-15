/*
 * Copyright (c) 2020 Adam Gaj
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.adgadev.jplusone.asserts.impl;

import com.adgadev.jplusone.asserts.api.JPlusOneAssertionContext;
import com.adgadev.jplusone.asserts.api.builder.ConditionBuilder;
import com.adgadev.jplusone.asserts.api.builder.ConditionDoneBuilder;
import com.adgadev.jplusone.asserts.api.builder.exclusion.complex.ImplicitOperationExclusionBuilder;
import com.adgadev.jplusone.asserts.api.builder.exclusion.simple.ExtendedImplicitOperationExclusionsBuilder;
import com.adgadev.jplusone.asserts.api.builder.exclusion.simple.SimpleImplicitOperationExclusionsBuilder;
import com.adgadev.jplusone.asserts.impl.rule.Rule;
import com.adgadev.jplusone.asserts.impl.rule.Condition;
import com.adgadev.jplusone.asserts.impl.rule.exclusion.ExclusionJoiningMode;
import com.adgadev.jplusone.asserts.impl.rule.exclusion.OperationExclusionList;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.function.Function;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class ExtendedImplicitOperationExclusionBuilderImpl implements SimpleImplicitOperationExclusionsBuilder, ExtendedImplicitOperationExclusionsBuilder {

    private final Rule rule;

    private final Condition condition;

    @Override
    public ConditionDoneBuilder exceptLoadingEntity(@NonNull Class<?> loadedEntityClass) {
        return exceptAnyOf(exclusions -> exclusions.loadingEntity(loadedEntityClass));
    }

    @Override
    public ConditionDoneBuilder exceptLoadingAnyEntity() {
        return exceptAnyOf(exclusions -> exclusions.loadingAnyEntity());
    }

    @Override
    public ConditionDoneBuilder exceptLoadingCollection(@NonNull Class<?> associationOwnerClass, @NonNull String associationField) {
        return exceptAnyOf(exclusions -> exclusions.loadingCollection(associationOwnerClass, associationField));
    }

    @Override
    public ConditionDoneBuilder exceptLoadingAnyCollection() {
        return exceptAnyOf(exclusions -> exclusions.loadingAnyCollection());
    }

    @Override
    public ConditionDoneBuilder exceptAnyOf(@NonNull Function<ImplicitOperationExclusionBuilder, ImplicitOperationExclusionBuilder> exclusions) {
        return exceptOf(exclusions, ExclusionJoiningMode.ANY);
    }

    @Override
    public ConditionDoneBuilder exceptAllOf(@NonNull Function<ImplicitOperationExclusionBuilder, ImplicitOperationExclusionBuilder> exclusions) {
        return exceptOf(exclusions, ExclusionJoiningMode.ALL);
    }

    @Override
    public ConditionDoneBuilder exceptAllOfInOrder(@NonNull Function<ImplicitOperationExclusionBuilder, ImplicitOperationExclusionBuilder> exclusions) {
        return exceptOf(exclusions, ExclusionJoiningMode.ALL_STRICT_ORDER);
    }

    private ConditionDoneBuilder exceptOf(Function<ImplicitOperationExclusionBuilder, ImplicitOperationExclusionBuilder> exclusions,
                                          ExclusionJoiningMode joiningMode) {
        OperationExclusionList operationExclusionList = OperationExclusionList.of(joiningMode);
        condition.setOperationExclusionList(operationExclusionList);

        ImplicitOperationExclusionBuilder implicitOperationExclusionBuilder = new ImplicitOperationExclusionBuilderImpl(operationExclusionList);
        exclusions.apply(implicitOperationExclusionBuilder);

        return new ConditionDoneBuilderImpl(rule);
    }

    @Override
    public ConditionBuilder andShouldBe() {
        return new ConditionBuilderImpl(rule, rule.newCondition());
    }

    @Override
    public void check(@NonNull JPlusOneAssertionContext context) {
        Assertion.of(rule, context).check();
    }
}
