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
import com.adgadev.jplusone.asserts.api.builder.exclusion.complex.ExplicitOperationExclusionBuilder;
import com.adgadev.jplusone.asserts.api.builder.exclusion.simple.ExtendedExplicitOperationExclusionsBuilder;
import com.adgadev.jplusone.asserts.api.builder.exclusion.simple.SimpleExplicitOperationExclusionsBuilder;
import com.adgadev.jplusone.asserts.impl.rule.Rule;
import com.adgadev.jplusone.asserts.impl.rule.Condition;
import com.adgadev.jplusone.asserts.impl.rule.exclusion.ExclusionJoiningMode;
import com.adgadev.jplusone.asserts.impl.rule.exclusion.OperationExclusionList;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.function.Function;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class ExtendedExplicitOperationExclusionBuilderImpl implements SimpleExplicitOperationExclusionsBuilder, ExtendedExplicitOperationExclusionsBuilder {

    private final Rule rule;

    private final Condition condition;

    @Override
    public ConditionDoneBuilder exceptFetchingData() {
        return exceptAnyOf(exclusions -> exclusions.fetchingData());
    }

    @Override
    public ConditionDoneBuilder exceptFetchingDataViaAnyMethodIn(@NonNull Class<?> clazz) {
        return exceptAnyOf(exclusions -> exclusions.fetchingDataViaAnyMethodIn(clazz));
    }

    @Override
    public ConditionDoneBuilder exceptFetchingDataVia(@NonNull Class<?> clazz, @NonNull String methodName) {
        return exceptAnyOf(exclusions -> exclusions.fetchingDataVia(clazz, methodName));
    }

    @Override
    public ConditionDoneBuilder exceptFetchingDataVia(@NonNull String className, @NonNull String methodName) {
        return exceptAnyOf(exclusions -> exclusions.fetchingDataVia(className, methodName));
    }

    @Override
    public ConditionDoneBuilder exceptAnyOf(@NonNull Function<ExplicitOperationExclusionBuilder, ExplicitOperationExclusionBuilder> exclusions) {
        return exceptOf(exclusions, ExclusionJoiningMode.ANY);
    }

    @Override
    public ConditionDoneBuilder exceptAllOf(@NonNull Function<ExplicitOperationExclusionBuilder, ExplicitOperationExclusionBuilder> exclusions) {
        return exceptOf(exclusions, ExclusionJoiningMode.ALL);
    }

    @Override
    public ConditionDoneBuilder exceptAllOfInOrder(@NonNull Function<ExplicitOperationExclusionBuilder, ExplicitOperationExclusionBuilder> exclusions) {
        return exceptOf(exclusions, ExclusionJoiningMode.ALL_STRICT_ORDER);
    }

    private ConditionDoneBuilder exceptOf(Function<ExplicitOperationExclusionBuilder, ExplicitOperationExclusionBuilder> exclusions,
                                          ExclusionJoiningMode joiningMode) {
        OperationExclusionList operationExclusionList = OperationExclusionList.of(joiningMode);
        condition.setOperationExclusionList(operationExclusionList);

        ExplicitOperationExclusionBuilder explicitOperationExclusionBuilder = new ExplicitOperationExclusionBuilderImpl(operationExclusionList);
        exclusions.apply(explicitOperationExclusionBuilder);

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
