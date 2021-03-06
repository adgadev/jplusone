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

package com.adgadev.jplusone.asserts.impl.rule.exclusion;

import com.adgadev.jplusone.asserts.api.builder.AmountMatcher;
import com.adgadev.jplusone.core.registry.OperationNodeView;
import com.adgadev.jplusone.core.registry.StatementNodeView;
import com.adgadev.jplusone.core.registry.StatementType;
import com.adgadev.jplusone.core.registry.StatementType.StatementGroupType;
import lombok.Setter;

public class ExplicitOperationExclusion implements OperationExclusion {

    @Setter
    private FetchingDataCriteria fetchingDataCriteria;

    @Setter
    private AmountMatcher fetchingDataAmountMatcher;

    @Override
    public boolean matchesOperation(OperationNodeView operation) {
        return isFetchOperation(operation) && operation.getCallFramesStack()
                .findLastMatchingFrame(fetchingDataCriteria::matches)
                .isPresent();
    }

    @Override
    public boolean matchesUseAmount(int exclusionUseAmount) {
        return fetchingDataAmountMatcher.apply(exclusionUseAmount);
    }

    private boolean isFetchOperation(OperationNodeView operation) {
        return operation.getStatements().stream()
                .map(StatementNodeView::getStatementType)
                .map(StatementType::getStatementGroupType)
                .anyMatch(statementGroupType -> statementGroupType == StatementGroupType.READ);
    }
}
