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

import com.adgadev.jplusone.core.registry.OperationNodeView;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.adgadev.jplusone.asserts.impl.util.CollectionUtils.getLastElement;
import static com.adgadev.jplusone.asserts.impl.util.CollectionUtils.getMaxCommonHeadFragmentOfLists;

@Getter
@RequiredArgsConstructor
public enum ExclusionJoiningMode {
    ANY(false) {
        @Override
        List<OperationNodeView> resolveExcludedOperationCandidates(List<OperationNodeView> operations, List<OperationNodeView> excludedOperationPreCandidates) {
            return excludedOperationPreCandidates;
        }

        List<OperationNodeView> resolveOperationsToCheckInNextIteration(List<OperationNodeView> operations, List<OperationNodeView> excludedOperationsPortion) {
            return operations;
        }
    },
    ALL(true) {
        @Override
        List<OperationNodeView> resolveExcludedOperationCandidates(List<OperationNodeView> operations, List<OperationNodeView> excludedOperationPreCandidates) {
            return excludedOperationPreCandidates;
        }

        List<OperationNodeView> resolveOperationsToCheckInNextIteration(List<OperationNodeView> operations, List<OperationNodeView> excludedOperationsPortion) {
            return operations;
        }
    },
    ALL_STRICT_ORDER(true) {
        @Override
        List<OperationNodeView> resolveExcludedOperationCandidates(List<OperationNodeView> operations, List<OperationNodeView> excludedOperationPreCandidates) {
            return getMaxCommonHeadFragmentOfLists(excludedOperationPreCandidates, operations);
        }

        List<OperationNodeView> resolveOperationsToCheckInNextIteration(List<OperationNodeView> operations, List<OperationNodeView> excludedOperationsPortion) {
            OperationNodeView lastExcludedOperation = getLastElement(excludedOperationsPortion);
            int lastProcessedOperationIndex = operations.indexOf(lastExcludedOperation);
            return operations.subList(lastProcessedOperationIndex + 1, operations.size());
        }
    };

    private final boolean exclusionApplicationRequired;

    abstract List<OperationNodeView> resolveExcludedOperationCandidates(List<OperationNodeView> currentOperations, List<OperationNodeView> excludedOperationPreCandidates);

    abstract List<OperationNodeView> resolveOperationsToCheckInNextIteration(List<OperationNodeView> currentOperations, List<OperationNodeView> nextExcludedOperationsPortion);
}
