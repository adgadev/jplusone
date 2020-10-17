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

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import static com.adgadev.jplusone.asserts.api.RuleTestUtils.expectFailingRule;
import static com.adgadev.jplusone.asserts.api.RuleTestUtils.expectMatchingRule;
import static com.adgadev.jplusone.asserts.context.mother.FrameExtractMother.anyFrameExtract;
import static com.adgadev.jplusone.asserts.context.mother.FrameStackMother.anyFrameStack;
import static com.adgadev.jplusone.asserts.context.mother.OperationNodeMother.anyExplicitOperationNode;
import static com.adgadev.jplusone.asserts.context.mother.OperationNodeMother.anyImplicitOperationNode;
import static com.adgadev.jplusone.asserts.context.mother.SessionNodeMother.anySessionNode;
import static com.adgadev.jplusone.asserts.context.mother.StatementNodeMother.anyInsertStatementNode;
import static com.adgadev.jplusone.asserts.context.mother.StatementNodeMother.anySelectStatementNode;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

class ExplicitOperationSimpleRuleTest {

    @TestFactory
    Iterable<DynamicTest> shouldAssertNoExplicitOperationRule() {
        JPlusOneAssertionRule rule = JPlusOneAssertionRule
                .within().lastSession()
                .shouldBe().noExplicitOperations();

        return asList(
                // POSITIVE test cases
                dynamicTest("[MATCHES] Empty session", expectMatchingRule(rule, anySessionNode(
                        // empty
                ))),
                dynamicTest("[MATCHES] Implicit operation", expectMatchingRule(rule, anySessionNode(
                        anyImplicitOperationNode()
                ))),
                // NEGATIVE test cases
                dynamicTest("[NOT MATCHES] Explicit operation", expectFailingRule(rule, anySessionNode(
                        anyExplicitOperationNode()
                )))
        );
    }

    @TestFactory
    Iterable<DynamicTest> shouldAssertNoExplicitOperationExceptFetchingDataRule() {
        JPlusOneAssertionRule rule = JPlusOneAssertionRule
                .within().lastSession()
                .shouldBe().noExplicitOperations().exceptFetchingData();

        return asList(
                // POSITIVE test cases
                dynamicTest("[MATCHES] Empty session", expectMatchingRule(rule, anySessionNode(
                        // empty
                ))),
                dynamicTest("[MATCHES] Implicit operation", expectMatchingRule(rule, anySessionNode(
                        anyImplicitOperationNode()
                ))),
                dynamicTest("[MATCHES] Fetching data", expectMatchingRule(rule, anySessionNode(
                        anyExplicitOperationNode(anySelectStatementNode())
                ))),
                // NEGATIVE test cases
                dynamicTest("[NOT MATCHES] Inserting data", expectFailingRule(rule, anySessionNode(
                        anyExplicitOperationNode(anyInsertStatementNode())
                )))
        );
    }

    @TestFactory
    Iterable<DynamicTest> shouldAssertNoExplicitOperationExceptFetchingDataViaClassMethodRule() {
        JPlusOneAssertionRule rule = JPlusOneAssertionRule
                .within().lastSession()
                .shouldBe().noExplicitOperations().exceptFetchingDataVia(SampleRepositoryA.class, "findById");

        return asList(
                // POSITIVE test cases
                dynamicTest("[MATCHES] Empty session", expectMatchingRule(rule, anySessionNode(
                        // empty
                ))),
                dynamicTest("[MATCHES] Implicit operation", expectMatchingRule(rule, anySessionNode(
                        anyImplicitOperationNode()
                ))),
                dynamicTest("[MATCHES] Fetching data via excluded class's method - interface", expectMatchingRule(rule, anySessionNode(
                        anyExplicitOperationNode(anySelectStatementNode(), anyFrameStack(
                                anyFrameExtract(SampleRepositoryA.class, "findById")
                        ))
                ))),
                dynamicTest("[MATCHES] Fetching data via excluded class's method - implementation", expectMatchingRule(rule, anySessionNode(
                        anyExplicitOperationNode(anySelectStatementNode(), anyFrameStack(
                                anyFrameExtract(SampleRepositoryAImpl.class, "findById")
                        ))
                ))),
                dynamicTest("[MATCHES] Fetching data via excluded class's method - subclass", expectMatchingRule(rule, anySessionNode(
                        anyExplicitOperationNode(anySelectStatementNode(), anyFrameStack(
                                anyFrameExtract(SampleRepositoryAImplSubclass.class, "findById")
                        ))
                ))),

                // NEGATIVE test cases
                dynamicTest("[NOT MATCHES] Fetching data via other method of the excluded class", expectFailingRule(rule, anySessionNode(
                        anyExplicitOperationNode(anySelectStatementNode(), anyFrameStack(
                                anyFrameExtract(SampleRepositoryA.class, "otherMethod")
                        ))
                ))),
                dynamicTest("[NOT MATCHES] Fetching data via other class's method", expectFailingRule(rule, anySessionNode(
                        anyExplicitOperationNode(anySelectStatementNode(), anyFrameStack(
                                anyFrameExtract(SampleRepositoryB.class, "findById")
                        ))
                ))),
                dynamicTest("[NOT MATCHES] Inserting data", expectFailingRule(rule, anySessionNode(
                        anyExplicitOperationNode(anyInsertStatementNode())
                )))
        );
    }

    @TestFactory
    Iterable<DynamicTest> shouldAssertNoExplicitOperationExceptFetchingDataViaAnyMethodOfClassRule() {
        JPlusOneAssertionRule rule = JPlusOneAssertionRule
                .within().lastSession()
                .shouldBe().noExplicitOperations().exceptFetchingDataViaAnyMethodIn(SampleRepositoryA.class);

        return asList(
                // POSITIVE test cases
                dynamicTest("[MATCHES] Empty session", expectMatchingRule(rule, anySessionNode(
                        // empty
                ))),
                dynamicTest("[MATCHES] Implicit operation", expectMatchingRule(rule, anySessionNode(
                        anyImplicitOperationNode()
                ))),
                dynamicTest("[MATCHES] Fetching data via excluded class's method - interface", expectMatchingRule(rule, anySessionNode(
                        anyExplicitOperationNode(anySelectStatementNode(), anyFrameStack(
                                anyFrameExtract(SampleRepositoryA.class, "findById")
                        ))
                ))),
                dynamicTest("[MATCHES] Fetching data via other method of the excluded class - interface", expectMatchingRule(rule, anySessionNode(
                        anyExplicitOperationNode(anySelectStatementNode(), anyFrameStack(
                                anyFrameExtract(SampleRepositoryA.class, "otherMethod")
                        ))
                ))),
                dynamicTest("[MATCHES] Fetching data via excluded class's method - implementation", expectMatchingRule(rule, anySessionNode(
                        anyExplicitOperationNode(anySelectStatementNode(), anyFrameStack(
                                anyFrameExtract(SampleRepositoryAImpl.class, "findById")
                        ))
                ))),
                dynamicTest("[MATCHES] Fetching data via excluded class's method - subclass", expectMatchingRule(rule, anySessionNode(
                        anyExplicitOperationNode(anySelectStatementNode(), anyFrameStack(
                                anyFrameExtract(SampleRepositoryAImplSubclass.class, "findById")
                        ))
                ))),

                // NEGATIVE test cases
                dynamicTest("[NOT MATCHES] Fetching data via other class's method", expectFailingRule(rule, anySessionNode(
                        anyExplicitOperationNode(anySelectStatementNode(), anyFrameStack(
                                anyFrameExtract(SampleRepositoryB.class, "findById")
                        ))
                ))),
                dynamicTest("[NOT MATCHES] Inserting data", expectFailingRule(rule, anySessionNode(
                        anyExplicitOperationNode(anyInsertStatementNode())
                )))
        );
    }

    @TestFactory
    Iterable<DynamicTest> shouldAssertNoExplicitOperationExceptFetchingDataViaClassNameAndMethodRule() {
        JPlusOneAssertionRule rule = JPlusOneAssertionRule
                .within().lastSession()
                .shouldBe().noExplicitOperations().exceptFetchingDataVia(SampleRepositoryA.class.getName(), "findById");

        return asList(
                // POSITIVE test cases
                dynamicTest("[MATCHES] Fetching data via excluded class's method - interface", expectMatchingRule(rule, anySessionNode(
                        anyExplicitOperationNode(anySelectStatementNode(), anyFrameStack(
                                anyFrameExtract(SampleRepositoryA.class, "findById")
                        ))
                ))),
                dynamicTest("[MATCHES] Fetching data via excluded class's method - implementation", expectMatchingRule(rule, anySessionNode(
                        anyExplicitOperationNode(anySelectStatementNode(), anyFrameStack(
                                anyFrameExtract(SampleRepositoryAImpl.class, "findById")
                        ))
                ))),
                dynamicTest("[MATCHES] Fetching data via excluded class's method - subclass", expectMatchingRule(rule, anySessionNode(
                        anyExplicitOperationNode(anySelectStatementNode(), anyFrameStack(
                                anyFrameExtract(SampleRepositoryAImplSubclass.class, "findById")
                        ))
                ))),

                // NEGATIVE test cases
                dynamicTest("[NOT MATCHES] Fetching data via other method of the excluded class", expectFailingRule(rule, anySessionNode(
                        anyExplicitOperationNode(anySelectStatementNode(), anyFrameStack(
                                anyFrameExtract(SampleRepositoryA.class, "otherMethod")
                        ))
                ))),
                dynamicTest("[NOT MATCHES] Fetching data via other class's method", expectFailingRule(rule, anySessionNode(
                        anyExplicitOperationNode(anySelectStatementNode(), anyFrameStack(
                                anyFrameExtract(SampleRepositoryB.class, "findById")
                        ))
                )))
        );
    }

    private interface SampleRepositoryA {
    }

    private interface SampleRepositoryB {
    }

    private static class SampleRepositoryAImpl implements SampleRepositoryA {
    }

    private static class SampleRepositoryAImplSubclass extends SampleRepositoryAImpl {
    }

}