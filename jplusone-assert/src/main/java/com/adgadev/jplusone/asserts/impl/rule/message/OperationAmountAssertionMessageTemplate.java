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
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
class OperationAmountAssertionMessageTemplate implements AmountAssertionMessageTemplate {

    private final SessionNodeView session;

    private final List<OperationNodeView> operations;

    private final OperationType operationType;

    @Override
    public String buildMessage(String expectedAmount, int actualAmount) {
        ReportFragmentGenerator reportFragmentGenerator = new ReportFragmentGenerator();
        String reportFragment = reportFragmentGenerator.generate(session, operations);

        return String.format("Actual amount of %s operations after applying exclusions is different than the expected amount\n" +
                        "    Expected: %s \n" +
                        "    Actual  : <%d>\n" +
                        "\n" +
                        "Operations after applying requested exclusions:%s",
                operationType, expectedAmount, actualAmount, reportFragment);
    }
}
