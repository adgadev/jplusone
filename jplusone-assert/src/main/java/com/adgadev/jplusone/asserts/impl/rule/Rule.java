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

package com.adgadev.jplusone.asserts.impl.rule;

import com.adgadev.jplusone.core.registry.OperationNodeView;
import com.adgadev.jplusone.core.registry.RootNodeView;
import com.adgadev.jplusone.core.registry.SessionNodeView;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class Rule {

    @Setter
    private SessionSelector sessionSelector;

    @Setter
    private ExecutionFilter executionFilter;

    private List<Condition> conditions = new ArrayList<>();

    public Condition newCondition() {
        Condition condition = new Condition();
        conditions.add(condition);
        return condition;
    }

    public void check(RootNodeView rootNode) {
        for (SessionNodeView session: sessionSelector.getMatchingSessions(rootNode)) {
            List<OperationNodeView> operations = executionFilter.getMatchingOperations(session);
            conditions.forEach(condition -> condition.check(session, operations));
        }
    }
}
