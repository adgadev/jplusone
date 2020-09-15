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
import com.adgadev.jplusone.asserts.api.builder.exclusion.complex.ExplicitOperationExclusionBuilder;
import com.adgadev.jplusone.asserts.api.builder.exclusion.complex.ExplicitOperationExclusionBuilder.ExplicitExclusionBuilderStage2;
import com.adgadev.jplusone.asserts.impl.rule.exclusion.ExplicitOperationExclusion;
import com.adgadev.jplusone.asserts.impl.rule.exclusion.OperationExclusionList;
import com.adgadev.jplusone.asserts.impl.rule.exclusion.FetchingDataCriteria;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import static com.adgadev.jplusone.asserts.api.builder.AmountMatcher.anyNumber;
import static com.adgadev.jplusone.asserts.impl.rule.exclusion.FetchingDataCriteria.fetchingDataViaCriteria;

@RequiredArgsConstructor
class ExplicitOperationExclusionBuilderImpl implements ExplicitOperationExclusionBuilder, ExplicitExclusionBuilderStage2 {

    private final OperationExclusionList operationExclusionList;

    private ExplicitOperationExclusion currentExclusion;

    @Override
    public ExplicitExclusionBuilderStage2 fetchingData() {
        currentExclusion = operationExclusionList.newExplicitOperationExclusion();
        currentExclusion.setFetchingDataAmountMatcher(anyNumber());
        currentExclusion.setFetchingDataCriteria(FetchingDataCriteria.fetchingDataCriteria());
        return this;
    }

    @Override
    public ExplicitExclusionBuilderStage2 fetchingDataViaAnyMethodIn(@NonNull Class<?> clazz) {
        currentExclusion = operationExclusionList.newExplicitOperationExclusion();
        currentExclusion.setFetchingDataAmountMatcher(anyNumber());
        currentExclusion.setFetchingDataCriteria(FetchingDataCriteria.fetchingDataViaAnyMethodCriteria(clazz));
        return this;
    }

    @Override
    public ExplicitExclusionBuilderStage2 fetchingDataVia(@NonNull Class<?> clazz, @NonNull String methodName) {
        currentExclusion = operationExclusionList.newExplicitOperationExclusion();
        currentExclusion.setFetchingDataAmountMatcher(anyNumber());
        currentExclusion.setFetchingDataCriteria(FetchingDataCriteria.fetchingDataViaCriteria(clazz, methodName));
        return this;
    }

    @Override
    public ExplicitExclusionBuilderStage2 fetchingDataVia(@NonNull String className, @NonNull String methodName) {
        currentExclusion = operationExclusionList.newExplicitOperationExclusion();
        currentExclusion.setFetchingDataAmountMatcher(anyNumber());
        currentExclusion.setFetchingDataCriteria(fetchingDataViaCriteria(className, methodName));
        return this;
    }

    @Override
    public ExplicitOperationExclusionBuilderImpl times(int amount) {
        currentExclusion.setFetchingDataAmountMatcher(AmountMatcher.exactly(amount));
        return new ExplicitOperationExclusionBuilderImpl(operationExclusionList);
    }

    @Override
    public ExplicitOperationExclusionBuilderImpl times(@NonNull AmountMatcher amountMatcher) {
        currentExclusion.setFetchingDataAmountMatcher(amountMatcher);
        return new ExplicitOperationExclusionBuilderImpl(operationExclusionList);
    }
}
