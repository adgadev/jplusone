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

package com.adgadev.jplusone.asserts.impl.rule.message;

import com.adgadev.jplusone.core.registry.OperationNodeView;
import com.adgadev.jplusone.core.registry.OperationType;
import com.adgadev.jplusone.core.registry.SessionNodeView;
import com.adgadev.jplusone.core.registry.StatementType;

import java.util.List;
import java.util.Set;

public interface AmountAssertionMessageTemplate {

    String buildMessage(String expectedAmount, int actualAmount);

    static AmountAssertionMessageTemplate forOperations(SessionNodeView session, List<OperationNodeView>operations,
                                                        OperationType operationType) {
        return new OperationAmountAssertionMessageTemplate(session, operations, operationType);
    }

    static AmountAssertionMessageTemplate forStatements(SessionNodeView session, List<OperationNodeView>operations,
                                                        Set<StatementType> statementTypes) {
        return new StatementAmountAssertionMessageTemplate(session, operations, statementTypes);
    }
}
