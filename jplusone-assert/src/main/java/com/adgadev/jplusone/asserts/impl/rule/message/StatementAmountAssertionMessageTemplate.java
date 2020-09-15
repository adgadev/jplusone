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
import com.adgadev.jplusone.core.registry.SessionNodeView;
import com.adgadev.jplusone.core.registry.StatementType;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
class StatementAmountAssertionMessageTemplate implements AmountAssertionMessageTemplate {

    private final SessionNodeView session;

    private final List<OperationNodeView> operations;

    private final Set<StatementType> statementTypes;

    @Override
    public String buildMessage(String expectedAmount, int actualAmount) {
        ReportFragmentGenerator reportFragmentGenerator = new ReportFragmentGenerator();
        String reportFragment = reportFragmentGenerator.generate(session, operations, statementTypes);

        return String.format("Actual amount of SQL statements (%s) is different than the expected amount\n" +
                        "    Expected: %s \n" +
                        "    Actual  : <%d>\n" +
                        "\n" +
                        "Operations & SQL statements after applying requested SQL statement type filter:%s",
                statementTypes, expectedAmount, actualAmount, reportFragment);
    }
}
