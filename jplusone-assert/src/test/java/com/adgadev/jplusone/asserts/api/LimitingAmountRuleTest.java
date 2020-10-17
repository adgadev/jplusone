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

import com.adgadev.jplusone.asserts.context.stub.SessionNodeStub;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import static com.adgadev.jplusone.asserts.api.RuleTestUtils.expectFailingRule;
import static com.adgadev.jplusone.asserts.api.RuleTestUtils.expectMatchingRule;
import static com.adgadev.jplusone.asserts.context.mother.OperationNodeMother.anyOperationNode;
import static com.adgadev.jplusone.asserts.context.mother.SessionNodeMother.anySessionNode;
import static com.adgadev.jplusone.asserts.context.mother.StatementNodeMother.anyInsertStatementNode;
import static com.adgadev.jplusone.asserts.context.mother.StatementNodeMother.anySelectStatementNode;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

public class LimitingAmountRuleTest {

    @TestFactory
    Iterable<DynamicTest> shouldAssertAmountOnSessionWithZeroStatements() {
        SessionNodeStub session = anySessionNode(
                // empty
        );

        return asList(
                // POSITIVE test cases
                dynamicTest("[MATCHES] none", expectMatchingRule(
                        JPlusOneAssertionRule.within().lastSession()
                                .shouldBe().none().sqlStatementsTotal(), session)),
                dynamicTest("[MATCHES] exactly 0", expectMatchingRule(
                        JPlusOneAssertionRule.within().lastSession()
                                .shouldBe().exactly(0).sqlStatementsTotal(), session)),
                dynamicTest("[MATCHES] at least 0", expectMatchingRule(
                        JPlusOneAssertionRule.within().lastSession()
                                .shouldBe().atLeast(0).sqlStatementsTotal(), session)),
                dynamicTest("[MATCHES] at most 0", expectMatchingRule(
                        JPlusOneAssertionRule.within().lastSession()
                                .shouldBe().atMost(0).sqlStatementsTotal(), session)),
                dynamicTest("[MATCHES] at most 1", expectMatchingRule(
                        JPlusOneAssertionRule.within().lastSession()
                                .shouldBe().atMost(1).sqlStatementsTotal(), session)),
                dynamicTest("[MATCHES] less than 1", expectMatchingRule(
                        JPlusOneAssertionRule.within().lastSession()
                                .shouldBe().lessThan(1).sqlStatementsTotal(), session)),
                dynamicTest("[MATCHES] none explicit operations", expectMatchingRule(
                        JPlusOneAssertionRule.within().lastSession()
                                .shouldBe().none().explicitOperations(), session)),

                // NEGATIVE test cases
                dynamicTest("[NOT MATCHES] exactly 1", expectFailingRule(
                        JPlusOneAssertionRule.within().lastSession()
                                .shouldBe().exactly(1).sqlStatementsTotal(), session)),
                dynamicTest("[NOT MATCHES] at least 1", expectFailingRule(
                        JPlusOneAssertionRule.within().lastSession()
                                .shouldBe().atLeast(1).sqlStatementsTotal(), session)),

                dynamicTest("[NOT MATCHES] more than 0", expectFailingRule(
                        JPlusOneAssertionRule.within().lastSession()
                                .shouldBe().moreThan(0).sqlStatementsTotal(), session)),
                dynamicTest("[NOT MATCHES] less than 0", expectFailingRule(
                        JPlusOneAssertionRule.within().lastSession()
                                .shouldBe().lessThan(0).sqlStatementsTotal(), session))
        );
    }

    @TestFactory
    Iterable<DynamicTest> shouldAssertAmountOnSessionWithOneStatement() {
        SessionNodeStub session = anySessionNode(
                anyOperationNode(anySelectStatementNode())
        );

        return asList(
                // POSITIVE test cases
                dynamicTest("[MATCHES] exactly 1", expectMatchingRule(
                        JPlusOneAssertionRule.within().lastSession()
                                .shouldBe().exactly(1).sqlStatementsTotal(), session)),
                dynamicTest("[MATCHES] at least 1", expectMatchingRule(
                        JPlusOneAssertionRule.within().lastSession()
                                .shouldBe().atLeast(1).sqlStatementsTotal(), session)),
                dynamicTest("[MATCHES] at most 1", expectMatchingRule(
                        JPlusOneAssertionRule.within().lastSession()
                                .shouldBe().atMost(1).sqlStatementsTotal(), session)),
                dynamicTest("[MATCHES] less than 2", expectMatchingRule(
                        JPlusOneAssertionRule.within().lastSession()
                                .shouldBe().lessThan(2).sqlStatementsTotal(), session)),
                dynamicTest("[MATCHES] more than 0", expectMatchingRule(
                        JPlusOneAssertionRule.within().lastSession()
                                .shouldBe().moreThan(0).sqlStatementsTotal(), session)),
                dynamicTest("[MATCHES] exactly one explicit operations", expectMatchingRule(
                        JPlusOneAssertionRule.within().lastSession()
                                .shouldBe().exactly(1).explicitOperations(), session)),

                // NEGATIVE test cases
                dynamicTest("[NOT MATCHES] none", expectFailingRule(
                        JPlusOneAssertionRule.within().lastSession()
                                .shouldBe().none().sqlStatementsTotal(), session)),
                dynamicTest("[NOT MATCHES] exactly 2", expectFailingRule(
                        JPlusOneAssertionRule.within().lastSession()
                                .shouldBe().exactly(2).sqlStatementsTotal(), session)),
                dynamicTest("[NOT MATCHES] at least 2", expectFailingRule(
                        JPlusOneAssertionRule.within().lastSession()
                                .shouldBe().atLeast(2).sqlStatementsTotal(), session)),
                dynamicTest("[NOT MATCHES] at most 0", expectFailingRule(
                        JPlusOneAssertionRule.within().lastSession()
                                .shouldBe().atMost(0).sqlStatementsTotal(), session)),
                dynamicTest("[NOT MATCHES] more than 1", expectFailingRule(
                        JPlusOneAssertionRule.within().lastSession()
                                .shouldBe().moreThan(1).sqlStatementsTotal(), session)),
                dynamicTest("[NOT MATCHES] less than 1", expectFailingRule(
                        JPlusOneAssertionRule.within().lastSession()
                                .shouldBe().lessThan(1).sqlStatementsTotal(), session))
        );
    }

    @TestFactory
    Iterable<DynamicTest> shouldAssertAmountOnSessionWithTwoStatements() {
        SessionNodeStub session = anySessionNode(
                anyOperationNode(anySelectStatementNode()),
                anyOperationNode(anyInsertStatementNode())
        );

        return asList(
                // POSITIVE test cases
                dynamicTest("[MATCHES] exactly 2", expectMatchingRule(
                        JPlusOneAssertionRule.within().lastSession()
                                .shouldBe().exactly(2).sqlStatementsTotal(), session)),
                dynamicTest("[MATCHES] at least 1", expectMatchingRule(
                        JPlusOneAssertionRule.within().lastSession()
                                .shouldBe().atLeast(1).sqlStatementsTotal(), session)),
                dynamicTest("[MATCHES] at least 2", expectMatchingRule(
                        JPlusOneAssertionRule.within().lastSession()
                                .shouldBe().atLeast(2).sqlStatementsTotal(), session)),
                dynamicTest("[MATCHES] at most 2", expectMatchingRule(
                        JPlusOneAssertionRule.within().lastSession()
                                .shouldBe().atMost(2).sqlStatementsTotal(), session)),
                dynamicTest("[MATCHES] at most 3", expectMatchingRule(
                        JPlusOneAssertionRule.within().lastSession()
                                .shouldBe().atMost(3).sqlStatementsTotal(), session)),
                dynamicTest("[MATCHES] less than 3", expectMatchingRule(
                        JPlusOneAssertionRule.within().lastSession()
                                .shouldBe().lessThan(3).sqlStatementsTotal(), session)),
                dynamicTest("[MATCHES] less than 4", expectMatchingRule(
                        JPlusOneAssertionRule.within().lastSession()
                                .shouldBe().lessThan(4).sqlStatementsTotal(), session)),
                dynamicTest("[MATCHES] more than 1", expectMatchingRule(
                        JPlusOneAssertionRule.within().lastSession()
                                .shouldBe().moreThan(1).sqlStatementsTotal(), session)),

                // NEGATIVE test cases
                dynamicTest("[NOT MATCHES] none", expectFailingRule(
                        JPlusOneAssertionRule.within().lastSession()
                                .shouldBe().none().sqlStatementsTotal(), session)),
                dynamicTest("[NOT MATCHES] exactly 1", expectFailingRule(
                        JPlusOneAssertionRule.within().lastSession()
                                .shouldBe().exactly(1).sqlStatementsTotal(), session)),
                dynamicTest("[NOT MATCHES] at least 3", expectFailingRule(
                        JPlusOneAssertionRule.within().lastSession()
                                .shouldBe().atLeast(3).sqlStatementsTotal(), session)),
                dynamicTest("[NOT MATCHES] at most 1", expectFailingRule(
                        JPlusOneAssertionRule.within().lastSession()
                                .shouldBe().atMost(1).sqlStatementsTotal(), session)),
                dynamicTest("[NOT MATCHES] more than 2", expectFailingRule(
                        JPlusOneAssertionRule.within().lastSession()
                                .shouldBe().moreThan(2).sqlStatementsTotal(), session)),
                dynamicTest("[NOT MATCHES] less than 1", expectFailingRule(
                        JPlusOneAssertionRule.within().lastSession()
                                .shouldBe().lessThan(1).sqlStatementsTotal(), session)),
                dynamicTest("[NOT MATCHES] less than 2", expectFailingRule(
                        JPlusOneAssertionRule.within().lastSession()
                                .shouldBe().lessThan(2).sqlStatementsTotal(), session))
        );
    }

}
