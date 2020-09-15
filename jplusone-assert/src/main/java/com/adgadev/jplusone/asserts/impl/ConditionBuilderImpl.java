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

import com.adgadev.jplusone.asserts.api.builder.AmountMatcher;
import com.adgadev.jplusone.asserts.api.builder.ConditionBuilder;
import com.adgadev.jplusone.asserts.api.builder.ShouldSectionBuilder;
import com.adgadev.jplusone.asserts.api.builder.SimpleConditionBuilder;
import com.adgadev.jplusone.asserts.api.builder.exclusion.simple.ExtendedExplicitOperationExclusionsBuilder;
import com.adgadev.jplusone.asserts.api.builder.exclusion.simple.ExtendedImplicitOperationExclusionsBuilder;
import com.adgadev.jplusone.asserts.impl.rule.AmountVerifier;
import com.adgadev.jplusone.asserts.impl.rule.Rule;
import com.adgadev.jplusone.asserts.impl.rule.Condition;
import com.adgadev.jplusone.core.registry.OperationType;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class ConditionBuilderImpl implements ShouldSectionBuilder, ConditionBuilder {

    private final Rule rule;

    private final Condition condition;

    @Override
    public ConditionBuilder shouldBe() {
        return this;
    }

    @Override
    public SimpleConditionBuilder none() {
        condition.setAmountVerifier(AmountVerifier.of(AmountMatcher.none(), "exactly", 0));
        return new SimpleConditionBuilderImpl(rule, condition);
    }

    @Override
    public SimpleConditionBuilder exactly(int amount) {
        condition.setAmountVerifier(AmountVerifier.of(AmountMatcher.exactly(amount), "exactly", amount));
        return new SimpleConditionBuilderImpl(rule, condition);
    }

    @Override
    public SimpleConditionBuilder atMost(int amount) {
        condition.setAmountVerifier(AmountVerifier.of(AmountMatcher.atMost(amount), "at most", amount));
        return new SimpleConditionBuilderImpl(rule, condition);
    }

    @Override
    public SimpleConditionBuilder atLeast(int amount) {
        condition.setAmountVerifier(AmountVerifier.of(AmountMatcher.atLeast(amount), "at least", amount));
        return new SimpleConditionBuilderImpl(rule, condition);
    }

    @Override
    public SimpleConditionBuilder moreThan(int amount) {
        condition.setAmountVerifier(AmountVerifier.of(AmountMatcher.moreThan(amount), "more than", amount));
        return new SimpleConditionBuilderImpl(rule, condition);
    }

    @Override
    public SimpleConditionBuilder lessThan(int amount) {
        condition.setAmountVerifier(AmountVerifier.of(AmountMatcher.lessThan(amount), "less than", amount));
        return new SimpleConditionBuilderImpl(rule, condition);
    }

    @Override
    public ExtendedImplicitOperationExclusionsBuilder noImplicitOperations() {
        condition.setAmountVerifier(AmountVerifier.of(AmountMatcher.none(), "exactly", 0));
        condition.setOperationType(OperationType.IMPLICIT);
        return new ExtendedImplicitOperationExclusionBuilderImpl(rule, condition);
    }

    @Override
    public ExtendedExplicitOperationExclusionsBuilder noExplicitOperations() {
        condition.setAmountVerifier(AmountVerifier.of(AmountMatcher.none(), "exactly", 0));
        condition.setOperationType(OperationType.EXPLICIT);
        return new ExtendedExplicitOperationExclusionBuilderImpl(rule, condition);
    }

}
