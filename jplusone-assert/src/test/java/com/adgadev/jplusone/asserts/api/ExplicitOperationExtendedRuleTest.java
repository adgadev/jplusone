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

class ExplicitOperationExtendedRuleTest {

    @TestFactory
    Iterable<DynamicTest> shouldAssertNoExplicitOperationExceptAnyOfFetchingDataRule() {
        JPlusOneAssertionRule rule = JPlusOneAssertionRule
                .within().lastSession()
                .shouldBe().noExplicitOperations().exceptAnyOf(exclusions -> exclusions
                        .fetchingData()
                );

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
    Iterable<DynamicTest> shouldAssertNoExplicitOperationExceptAnyOfFetchingDataViaClassMethodRule() {
        JPlusOneAssertionRule rule = JPlusOneAssertionRule
                .within().lastSession()
                .shouldBe().noExplicitOperations().exceptAnyOf(exclusions -> exclusions
                        .fetchingDataVia(SampleRepositoryA.class, "findById")
                );

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
    Iterable<DynamicTest> shouldAssertNoExplicitOperationExceptAnyOfFetchingDataViaAnyMethodOfClassRule() {
        JPlusOneAssertionRule rule = JPlusOneAssertionRule
                .within().lastSession()
                .shouldBe().noExplicitOperations().exceptAnyOf(exclusions -> exclusions
                        .fetchingDataViaAnyMethodIn(SampleRepositoryA.class)
                );

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
    Iterable<DynamicTest> shouldAssertNoExplicitOperationExceptAnyOfFetchingDataViaClassNameAndMethodRule() {
        JPlusOneAssertionRule rule = JPlusOneAssertionRule
                .within().lastSession()
                .shouldBe().noExplicitOperations().exceptAnyOf(exclusions -> exclusions
                        .fetchingDataVia(SampleRepositoryA.class.getName(), "findById")
                );

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

    @TestFactory
    Iterable<DynamicTest> shouldAssertNoExplicitOperationExceptAnyOfMultipleSpecifiedRule() {
        JPlusOneAssertionRule rule = JPlusOneAssertionRule
                .within().lastSession()
                .shouldBe().noExplicitOperations().exceptAnyOf(exclusions -> exclusions
                        .fetchingDataVia(SampleRepositoryA.class, "findById")
                        .fetchingDataVia(SampleRepositoryB.class, "findByMasterId")
                );

        return asList(
                // POSITIVE test cases
                dynamicTest("[MATCHES] Fetching data via A.findById", expectMatchingRule(rule, anySessionNode(
                        anyExplicitOperationNode(anySelectStatementNode(), anyFrameStack(
                                anyFrameExtract(SampleRepositoryA.class, "findById")
                        ))
                ))),
                dynamicTest("[MATCHES] Fetching data via A.findById", expectMatchingRule(rule, anySessionNode(
                        anyExplicitOperationNode(anySelectStatementNode(), anyFrameStack(
                                anyFrameExtract(SampleRepositoryB.class, "findByMasterId")
                        ))
                ))),
                dynamicTest("[MATCHES] Fetching data via A.findById, B.findByMasterId", expectMatchingRule(rule, anySessionNode(
                        anyExplicitOperationNode(anySelectStatementNode(), anyFrameStack(
                                anyFrameExtract(SampleRepositoryA.class, "findById")
                        )),
                        anyExplicitOperationNode(anySelectStatementNode(), anyFrameStack(
                                anyFrameExtract(SampleRepositoryB.class, "findByMasterId")
                        ))
                ))),
                dynamicTest("[MATCHES] Fetching data via B.findByMasterId, A.findById", expectMatchingRule(rule, anySessionNode(
                        anyExplicitOperationNode(anySelectStatementNode(), anyFrameStack(
                                anyFrameExtract(SampleRepositoryB.class, "findByMasterId")
                        )),
                        anyExplicitOperationNode(anySelectStatementNode(), anyFrameStack(
                                anyFrameExtract(SampleRepositoryA.class, "findById")
                        ))
                ))),
                dynamicTest("[MATCHES] Fetching data via A.findById, A.findById", expectMatchingRule(rule, anySessionNode(
                        anyExplicitOperationNode(anySelectStatementNode(), anyFrameStack(
                                anyFrameExtract(SampleRepositoryA.class, "findById")
                        )),
                        anyExplicitOperationNode(anySelectStatementNode(), anyFrameStack(
                                anyFrameExtract(SampleRepositoryA.class, "findById")
                        ))
                ))),
                dynamicTest("[MATCHES] Fetching data via A.findById, B.findByMasterId, A.findById", expectMatchingRule(rule, anySessionNode(
                        anyExplicitOperationNode(anySelectStatementNode(), anyFrameStack(
                                anyFrameExtract(SampleRepositoryA.class, "findById")
                        )),
                        anyExplicitOperationNode(anySelectStatementNode(), anyFrameStack(
                                anyFrameExtract(SampleRepositoryB.class, "findByMasterId")
                        )),
                        anyExplicitOperationNode(anySelectStatementNode(), anyFrameStack(
                                anyFrameExtract(SampleRepositoryA.class, "findById")
                        ))
                ))),

                // NEGATIVE test cases
                dynamicTest("[NOT MATCHES] Fetching data via A.otherMethod", expectFailingRule(rule, anySessionNode(
                        anyExplicitOperationNode(anySelectStatementNode(), anyFrameStack(
                                anyFrameExtract(SampleRepositoryA.class, "otherMethod")
                        ))
                ))),
                dynamicTest("[NOT MATCHES] Fetching data via B.findById", expectFailingRule(rule, anySessionNode(
                        anyExplicitOperationNode(anySelectStatementNode(), anyFrameStack(
                                anyFrameExtract(SampleRepositoryB.class, "findById")
                        ))
                )))
        );
    }

    @TestFactory
    Iterable<DynamicTest> shouldAssertNoExplicitOperationExceptAllOfMultipleSpecifiedRule() {
        JPlusOneAssertionRule rule = JPlusOneAssertionRule
                .within().lastSession()
                .shouldBe().noExplicitOperations().exceptAllOf(exclusions -> exclusions
                        .fetchingDataVia(SampleRepositoryA.class, "findById")
                        .fetchingDataVia(SampleRepositoryB.class, "findByMasterId")
                );

        return asList(
                // POSITIVE test cases
                dynamicTest("[MATCHES] Fetching data via A.findById, B.findByMasterId", expectMatchingRule(rule, anySessionNode(
                        anyExplicitOperationNode(anySelectStatementNode(), anyFrameStack(
                                anyFrameExtract(SampleRepositoryA.class, "findById")
                        )),
                        anyExplicitOperationNode(anySelectStatementNode(), anyFrameStack(
                                anyFrameExtract(SampleRepositoryB.class, "findByMasterId")
                        ))
                ))),
                dynamicTest("[MATCHES] Fetching data via B.findByMasterId, A.findById", expectMatchingRule(rule, anySessionNode(
                        anyExplicitOperationNode(anySelectStatementNode(), anyFrameStack(
                                anyFrameExtract(SampleRepositoryB.class, "findByMasterId")
                        )),
                        anyExplicitOperationNode(anySelectStatementNode(), anyFrameStack(
                                anyFrameExtract(SampleRepositoryA.class, "findById")
                        ))
                ))),
                dynamicTest("[MATCHES] Fetching data via A.findById, B.findByMasterId, A.findById", expectMatchingRule(rule, anySessionNode(
                        anyExplicitOperationNode(anySelectStatementNode(), anyFrameStack(
                                anyFrameExtract(SampleRepositoryA.class, "findById")
                        )),
                        anyExplicitOperationNode(anySelectStatementNode(), anyFrameStack(
                                anyFrameExtract(SampleRepositoryB.class, "findByMasterId")
                        )),
                        anyExplicitOperationNode(anySelectStatementNode(), anyFrameStack(
                                anyFrameExtract(SampleRepositoryA.class, "findById")
                        ))
                ))),

                // NEGATIVE test cases
                dynamicTest("[NOT MATCHES] Fetching data via A.findById", expectFailingRule(rule, anySessionNode(
                        anyExplicitOperationNode(anySelectStatementNode(), anyFrameStack(
                                anyFrameExtract(SampleRepositoryA.class, "findById")
                        ))
                ))),
                dynamicTest("[NOT MATCHES] Fetching data via A.findById", expectFailingRule(rule, anySessionNode(
                        anyExplicitOperationNode(anySelectStatementNode(), anyFrameStack(
                                anyFrameExtract(SampleRepositoryB.class, "findByMasterId")
                        ))
                ))),
                dynamicTest("[NOT MATCHES] Fetching data via A.findById, A.findById", expectFailingRule(rule, anySessionNode(
                        anyExplicitOperationNode(anySelectStatementNode(), anyFrameStack(
                                anyFrameExtract(SampleRepositoryA.class, "findById")
                        )),
                        anyExplicitOperationNode(anySelectStatementNode(), anyFrameStack(
                                anyFrameExtract(SampleRepositoryA.class, "findById")
                        ))
                ))),
                dynamicTest("[NOT MATCHES] Fetching data via A.otherMethod", expectFailingRule(rule, anySessionNode(
                        anyExplicitOperationNode(anySelectStatementNode(), anyFrameStack(
                                anyFrameExtract(SampleRepositoryA.class, "otherMethod")
                        ))
                ))),
                dynamicTest("[NOT MATCHES] Fetching data via B.findById", expectFailingRule(rule, anySessionNode(
                        anyExplicitOperationNode(anySelectStatementNode(), anyFrameStack(
                                anyFrameExtract(SampleRepositoryB.class, "findById")
                        ))
                )))
        );
    }

    @TestFactory
    Iterable<DynamicTest> shouldAssertNoExplicitOperationExceptAllOfInOrderMultipleSpecifiedRule() {
        JPlusOneAssertionRule rule = JPlusOneAssertionRule
                .within().lastSession()
                .shouldBe().noExplicitOperations().exceptAllOfInOrder(exclusions -> exclusions
                        .fetchingDataVia(SampleRepositoryA.class, "findById")
                        .fetchingDataVia(SampleRepositoryB.class, "findByMasterId")
                );

        return asList(
                // POSITIVE test cases
                dynamicTest("[MATCHES] Fetching data via A.findById, B.findByMasterId", expectMatchingRule(rule, anySessionNode(
                        anyExplicitOperationNode(anySelectStatementNode(), anyFrameStack(
                                anyFrameExtract(SampleRepositoryA.class, "findById")
                        )),
                        anyExplicitOperationNode(anySelectStatementNode(), anyFrameStack(
                                anyFrameExtract(SampleRepositoryB.class, "findByMasterId")
                        ))
                ))),
                dynamicTest("[MATCHES] Fetching data via 2x A.findById, 2x B.findByMasterId", expectMatchingRule(rule, anySessionNode(
                        anyExplicitOperationNode(anySelectStatementNode(), anyFrameStack(
                                anyFrameExtract(SampleRepositoryA.class, "findById")
                        )),
                        anyExplicitOperationNode(anySelectStatementNode(), anyFrameStack(
                                anyFrameExtract(SampleRepositoryA.class, "findById")
                        )),
                        anyExplicitOperationNode(anySelectStatementNode(), anyFrameStack(
                                anyFrameExtract(SampleRepositoryB.class, "findByMasterId")
                        )),
                        anyExplicitOperationNode(anySelectStatementNode(), anyFrameStack(
                                anyFrameExtract(SampleRepositoryB.class, "findByMasterId")
                        ))
                ))),

                // NEGATIVE test cases
                dynamicTest("[NOT MATCHES] Fetching data via A.findById", expectFailingRule(rule, anySessionNode(
                        anyExplicitOperationNode(anySelectStatementNode(), anyFrameStack(
                                anyFrameExtract(SampleRepositoryA.class, "findById")
                        ))
                ))),
                dynamicTest("[NOT MATCHES] Fetching data via A.findById", expectFailingRule(rule, anySessionNode(
                        anyExplicitOperationNode(anySelectStatementNode(), anyFrameStack(
                                anyFrameExtract(SampleRepositoryB.class, "findByMasterId")
                        ))
                ))),
                dynamicTest("[NOT MATCHES] Fetching data via A.findById, A.findById", expectFailingRule(rule, anySessionNode(
                        anyExplicitOperationNode(anySelectStatementNode(), anyFrameStack(
                                anyFrameExtract(SampleRepositoryA.class, "findById")
                        )),
                        anyExplicitOperationNode(anySelectStatementNode(), anyFrameStack(
                                anyFrameExtract(SampleRepositoryA.class, "findById")
                        ))
                ))),
                dynamicTest("[NOT MATCHES] Fetching data via B.findByMasterId, A.findById", expectFailingRule(rule, anySessionNode(
                        anyExplicitOperationNode(anySelectStatementNode(), anyFrameStack(
                                anyFrameExtract(SampleRepositoryB.class, "findByMasterId")
                        )),
                        anyExplicitOperationNode(anySelectStatementNode(), anyFrameStack(
                                anyFrameExtract(SampleRepositoryA.class, "findById")
                        ))
                ))),
                dynamicTest("[NOT MATCHES] Fetching data via A.findById, B.findByMasterId, A.findById", expectFailingRule(rule, anySessionNode(
                        anyExplicitOperationNode(anySelectStatementNode(), anyFrameStack(
                                anyFrameExtract(SampleRepositoryA.class, "findById")
                        )),
                        anyExplicitOperationNode(anySelectStatementNode(), anyFrameStack(
                                anyFrameExtract(SampleRepositoryB.class, "findByMasterId")
                        )),
                        anyExplicitOperationNode(anySelectStatementNode(), anyFrameStack(
                                anyFrameExtract(SampleRepositoryA.class, "findById")
                        ))
                ))),
                dynamicTest("[NOT MATCHES] Fetching data via A.otherMethod", expectFailingRule(rule, anySessionNode(
                        anyExplicitOperationNode(anySelectStatementNode(), anyFrameStack(
                                anyFrameExtract(SampleRepositoryA.class, "otherMethod")
                        ))
                ))),
                dynamicTest("[NOT MATCHES] Fetching data via B.findById", expectFailingRule(rule, anySessionNode(
                        anyExplicitOperationNode(anySelectStatementNode(), anyFrameStack(
                                anyFrameExtract(SampleRepositoryB.class, "findById")
                        ))
                )))
        );
    }

    @TestFactory
    Iterable<DynamicTest> shouldAssertNoExplicitOperationExceptAllOfInOrderThreeSpecifiedRule() {
        JPlusOneAssertionRule rule = JPlusOneAssertionRule
                .within().lastSession()
                .shouldBe().noExplicitOperations().exceptAnyOf(exclusions -> exclusions
                        .fetchingDataVia(SampleRepositoryA.class, "findById")
                        .fetchingDataVia(SampleRepositoryA.class, "findByParentId")
                        .fetchingDataViaAnyMethodIn(SampleRepositoryB.class)
                );

        return asList(
                // POSITIVE test cases
                dynamicTest("[MATCHES] Fetching data via A.findById", expectMatchingRule(rule, anySessionNode(
                        anyExplicitOperationNode(anySelectStatementNode(), anyFrameStack(
                                anyFrameExtract(SampleRepositoryA.class, "findById")
                        ))
                ))),
                dynamicTest("[MATCHES] Fetching data via A.findByParentId", expectMatchingRule(rule, anySessionNode(
                        anyExplicitOperationNode(anySelectStatementNode(), anyFrameStack(
                                anyFrameExtract(SampleRepositoryA.class, "findByParentId")
                        ))
                ))),
                dynamicTest("[MATCHES] Fetching data via B.someMethod", expectMatchingRule(rule, anySessionNode(
                        anyExplicitOperationNode(anySelectStatementNode(), anyFrameStack(
                                anyFrameExtract(SampleRepositoryB.class, "someMethod")
                        ))
                ))),
                dynamicTest("[MATCHES] Fetching data via B.someOtherMethod", expectMatchingRule(rule, anySessionNode(
                        anyExplicitOperationNode(anySelectStatementNode(), anyFrameStack(
                                anyFrameExtract(SampleRepositoryB.class, "someOtherMethod")
                        ))
                ))),
                dynamicTest("[MATCHES] Fetching data via A.findByParentId, A.findById, B.load", expectMatchingRule(rule, anySessionNode(
                        anyExplicitOperationNode(anySelectStatementNode(), anyFrameStack(
                                anyFrameExtract(SampleRepositoryA.class, "findByParentId")
                        )),
                        anyExplicitOperationNode(anySelectStatementNode(), anyFrameStack(
                                anyFrameExtract(SampleRepositoryA.class, "findById")
                        )),
                        anyExplicitOperationNode(anySelectStatementNode(), anyFrameStack(
                                anyFrameExtract(SampleRepositoryB.class, "load")
                        ))
                ))),

                // NEGATIVE test cases
                dynamicTest("[NOT MATCHES] Fetching data via A.otherMethod", expectFailingRule(rule, anySessionNode(
                        anyExplicitOperationNode(anySelectStatementNode(), anyFrameStack(
                                anyFrameExtract(SampleRepositoryA.class, "otherMethod")
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