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

package com.adgadev.jplusone.asserts.api;

import com.adgadev.jplusone.asserts.api.builder.SqlStatementGroupType;
import com.adgadev.jplusone.asserts.api.builder.SqlStatementType;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import static com.adgadev.jplusone.asserts.api.RuleTestUtils.expectFailingRule;
import static com.adgadev.jplusone.asserts.api.RuleTestUtils.expectMatchingRule;
import static com.adgadev.jplusone.asserts.context.mother.OperationNodeMother.anyOperationNode;
import static com.adgadev.jplusone.asserts.context.mother.SessionNodeMother.anySessionNode;
import static com.adgadev.jplusone.asserts.context.mother.StatementNodeMother.*;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

public class SqlStatementCountRuleTest {

    @TestFactory
    Iterable<DynamicTest> shouldAssertNoSqlStatementsInGeneralTotalRule() {
        JPlusOneAssertionRule rule = JPlusOneAssertionRule
                .within().lastSession()
                .shouldBe().none().sqlStatementsTotal();

        return asList(
                // POSITIVE test cases
                dynamicTest("[MATCHES] Empty session", expectMatchingRule(rule, anySessionNode(
                        // empty
                ))),
                // NEGATIVE test cases
                dynamicTest("[NOT MATCHES] Any operation with SQL statement", expectFailingRule(rule, anySessionNode(
                        anyOperationNode(anySelectStatementNode())
                )))
        );
    }

    @TestFactory
    Iterable<DynamicTest> shouldAssertNoInsertSqlStatementsOfSpecifiedTypeTotalRule() {
        JPlusOneAssertionRule rule = JPlusOneAssertionRule
                .within().lastSession()
                .shouldBe().none().sqlStatementsTotal(SqlStatementType.INSERT_STATEMENT);

        return asList(
                // POSITIVE test cases
                dynamicTest("[MATCHES] Empty session", expectMatchingRule(rule, anySessionNode(
                        // empty
                ))),
                dynamicTest("[MATCHES] operation with 1xSELECT", expectMatchingRule(rule, anySessionNode(
                        anyOperationNode(anySelectStatementNode())
                ))),
                dynamicTest("[MATCHES] operation with 1xUPDATE", expectMatchingRule(rule, anySessionNode(
                        anyOperationNode(anyUpdateStatementNode())
                ))),
                dynamicTest("[MATCHES] operation with 2xSELECT", expectMatchingRule(rule, anySessionNode(
                        anyOperationNode(anySelectStatementNode(), anySelectStatementNode())
                ))),
                // NEGATIVE test cases
                dynamicTest("[NOT MATCHES] operation with 1xINSERT", expectFailingRule(rule, anySessionNode(
                        anyOperationNode(anyInsertStatementNode())
                )))
        );
    }

    @TestFactory
    Iterable<DynamicTest> shouldAssertNoInsertSqlStatementsOfSpecifiedGroupTypeTotalRule() {
        JPlusOneAssertionRule rule = JPlusOneAssertionRule
                .within().lastSession()
                .shouldBe().none().sqlStatementsTotal(SqlStatementGroupType.WRITE_STATEMENTS);

        return asList(
                // POSITIVE test cases
                dynamicTest("[MATCHES] Empty session", expectMatchingRule(rule, anySessionNode(
                        // empty
                ))),
                dynamicTest("[MATCHES] operation with 1xSELECT", expectMatchingRule(rule, anySessionNode(
                        anyOperationNode(anySelectStatementNode())
                ))),
                dynamicTest("[MATCHES] operation with 2xSELECT", expectMatchingRule(rule, anySessionNode(
                        anyOperationNode(anySelectStatementNode(), anySelectStatementNode())
                ))),
                // NEGATIVE test cases
                dynamicTest("[NOT MATCHES] operation with 1xUPDATE", expectFailingRule(rule, anySessionNode(
                        anyOperationNode(anyUpdateStatementNode())
                ))),
                dynamicTest("[NOT MATCHES] operation with 1xINSERT", expectFailingRule(rule, anySessionNode(
                        anyOperationNode(anyInsertStatementNode())
                )))
        );
    }

}
