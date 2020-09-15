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
import com.adgadev.jplusone.asserts.api.builder.exclusion.complex.ImplicitOperationExclusionBuilder;
import com.adgadev.jplusone.asserts.api.builder.exclusion.complex.ImplicitOperationExclusionBuilder.ImplicitExclusionBuilderStage2;
import com.adgadev.jplusone.asserts.impl.rule.exclusion.ImplicitOperationExclusion;
import com.adgadev.jplusone.asserts.impl.rule.exclusion.OperationExclusionList;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import static com.adgadev.jplusone.asserts.api.builder.AmountMatcher.anyNumber;
import static com.adgadev.jplusone.asserts.impl.rule.exclusion.LazyInitialisationCriteria.*;

@RequiredArgsConstructor
class ImplicitOperationExclusionBuilderImpl implements ImplicitOperationExclusionBuilder, ImplicitExclusionBuilderStage2 {

    private final OperationExclusionList operationExclusionList;

    private ImplicitOperationExclusion currentExclusion;

    @Override
    public ImplicitExclusionBuilderStage2 loadingEntity(@NonNull Class<?> entityClazz) {
        currentExclusion = operationExclusionList.newImplicitOperationExclusion();
        currentExclusion.setLazyInitialisationAmountMatcher(anyNumber());
        currentExclusion.setLazyInitialisationCriteria(loadingEntityCriteria(entityClazz));
        return this;
    }

    @Override
    public ImplicitExclusionBuilderStage2 loadingAnyEntity() {
        currentExclusion = operationExclusionList.newImplicitOperationExclusion();
        currentExclusion.setLazyInitialisationAmountMatcher(anyNumber());
        currentExclusion.setLazyInitialisationCriteria(loadingAnyEntityCriteria());
        return this;
    }

    @Override
    public ImplicitExclusionBuilderStage2 loadingCollection(@NonNull Class<?> entityClass, @NonNull String associationField) {
        currentExclusion = operationExclusionList.newImplicitOperationExclusion();
        currentExclusion.setLazyInitialisationAmountMatcher(anyNumber());
        currentExclusion.setLazyInitialisationCriteria(loadingCollectionCriteria(entityClass, associationField));
        return this;
    }

    @Override
    public ImplicitExclusionBuilderStage2 loadingAnyCollectionInEntity(@NonNull Class<?> entityClass) {
        currentExclusion = operationExclusionList.newImplicitOperationExclusion();
        currentExclusion.setLazyInitialisationAmountMatcher(anyNumber());
        currentExclusion.setLazyInitialisationCriteria(loadingAnyCollectionInEntityCriteria(entityClass));
        return this;
    }

    @Override
    public ImplicitExclusionBuilderStage2 loadingAnyCollection() {
        currentExclusion = operationExclusionList.newImplicitOperationExclusion();
        currentExclusion.setLazyInitialisationAmountMatcher(anyNumber());
        currentExclusion.setLazyInitialisationCriteria(loadingAnyCollectionCriteria());
        return this;
    }

    @Override
    public ImplicitOperationExclusionBuilder times(int amount) {
        currentExclusion.setLazyInitialisationAmountMatcher(AmountMatcher.exactly(amount));
        return new ImplicitOperationExclusionBuilderImpl(operationExclusionList);
    }

    @Override
    public ImplicitOperationExclusionBuilder times(@NonNull AmountMatcher amountMatcher) {
        currentExclusion.setLazyInitialisationAmountMatcher(amountMatcher);
        return new ImplicitOperationExclusionBuilderImpl(operationExclusionList);
    }
}
