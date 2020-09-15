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

import com.adgadev.jplusone.asserts.api.builder.ConditionBuilder;
import com.adgadev.jplusone.asserts.api.builder.ExecutionFilterBuilder;
import com.adgadev.jplusone.asserts.api.builder.ShouldSectionBuilder;
import com.adgadev.jplusone.asserts.impl.rule.Rule;
import com.adgadev.jplusone.asserts.impl.rule.ExecutionFilter;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class ExecutionFilterBuilderImpl implements ExecutionFilterBuilder {

    private final Rule rule;

    @Override
    public ShouldSectionBuilder insideExecutionOfMethod(@NonNull Class<?> clazz, @NonNull String methodName) {
        rule.setExecutionFilter(ExecutionFilter.insideExecutionOfMethod(clazz, methodName));
        return new ConditionBuilderImpl(rule, rule.newCondition());
    }

    @Override
    public ShouldSectionBuilder insideExecutionOfAnyMethodIn(@NonNull Class<?>... classes) {
        rule.setExecutionFilter(ExecutionFilter.insideExecutionOfAnyMethodIn(classes));
        return new ConditionBuilderImpl(rule, rule.newCondition());
    }

    @Override
    public ConditionBuilder shouldBe() {
        rule.setExecutionFilter(ExecutionFilter.insideExecutionOfAnyMethod());
        return new ConditionBuilderImpl(rule, rule.newCondition());
    }
}
