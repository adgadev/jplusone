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
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static com.adgadev.jplusone.core.utils.ReflectionUtils.not;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor(staticName = "of")
public class OperationExclusionList {

    private final List<OperationExclusion> exclusions = new ArrayList<>();

    private final ExclusionJoiningMode joiningMode;

    public ImplicitOperationExclusion newImplicitOperationExclusion() {
        ImplicitOperationExclusion exclusion = new ImplicitOperationExclusion();
        exclusions.add(exclusion);
        return exclusion;
    }

    public ExplicitOperationExclusion newExplicitOperationExclusion() {
        ExplicitOperationExclusion exclusion = new ExplicitOperationExclusion();
        exclusions.add(exclusion);
        return exclusion;
    }

    public List<OperationNodeView> filter(List<OperationNodeView> initialOperations) {
        List<OperationNodeView> operations = initialOperations;
        Set<OperationNodeView> excludedOperations = new HashSet<>();

        for (OperationExclusion exclusion : exclusions) {
            List<OperationNodeView> currentOperations = operations;
            List<OperationNodeView> excludedOperationsCandidates = operations.stream()
                    .filter(not(excludedOperations::contains))
                    .filter(exclusion::matchesOperation)
                    .collect(collectingAndThen(toList(), candidates -> joiningMode.resolveExcludedOperationCandidates(currentOperations, candidates)));

            int maxExclusionUseAmount = findMaximizedExclusionUseAmount(exclusion, excludedOperationsCandidates.size());

            if (maxExclusionUseAmount > 0) {
                List<OperationNodeView> excludedOperationsPortion = excludedOperationsCandidates.subList(0, maxExclusionUseAmount);
                excludedOperations.addAll(excludedOperationsPortion);
                operations = joiningMode.resolveOperationsToCheckInNextIteration(operations, excludedOperationsPortion);

            } else {
                if (joiningMode.isExclusionApplicationRequired()) {
                    return initialOperations;
                }
            }
        }

        return initialOperations.stream()
                .filter(not(excludedOperations::contains))
                .collect(toList());
    }

    private int findMaximizedExclusionUseAmount(OperationExclusion exclusion, Integer amountLimit) {
        return Stream.iterate(amountLimit, i -> i - 1)
                .takeWhile(i -> i > 0)
                .filter(exclusion::matchesUseAmount)
                .findFirst()
                .orElse(0);
    }
}
