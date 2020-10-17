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

import com.adgadev.jplusone.asserts.context.mother.OperationNodeMother.SampleEntityA;
import com.adgadev.jplusone.asserts.context.mother.OperationNodeMother.SampleEntityB;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import static com.adgadev.jplusone.asserts.api.RuleTestUtils.expectFailingRule;
import static com.adgadev.jplusone.asserts.api.RuleTestUtils.expectMatchingRule;
import static com.adgadev.jplusone.asserts.api.builder.AmountMatcher.*;
import static com.adgadev.jplusone.asserts.context.mother.LazyInitializationMother.anyCollectionLazyInitialisation;
import static com.adgadev.jplusone.asserts.context.mother.LazyInitializationMother.anyEntityLazyInitialisation;
import static com.adgadev.jplusone.asserts.context.mother.OperationNodeMother.anyImplicitOperationNode;
import static com.adgadev.jplusone.asserts.context.mother.SessionNodeMother.anySessionNode;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

public class TimesExtendedRuleTest {

    @TestFactory
    Iterable<DynamicTest> shouldAssertNoImplicitOperationExceptAnyOfMultipleWithTimesAtMostRule() {
        JPlusOneAssertionRule rule = JPlusOneAssertionRule
                .within().lastSession()
                .shouldBe().noImplicitOperations().exceptAnyOf(exclusions -> exclusions
                        .loadingAnyEntity().times(atMost(2))
                        .loadingAnyCollection().times(atMost(1))
                );

        return asList(
                // POSITIVE test cases
                dynamicTest("[MATCHES] empty session", expectMatchingRule(rule, anySessionNode(
                ))),
                dynamicTest("[MATCHES] loading A", expectMatchingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class))
                ))),
                dynamicTest("[MATCHES] loading A,B", expectMatchingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityB.class))
                ))),
                dynamicTest("[NOT MATCHES] loading A.tags", expectMatchingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityA.class, "tags"))
                ))),
                dynamicTest("[MATCHES] loading A, A.tags", expectMatchingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityA.class, "tags"))
                ))),
                dynamicTest("[MATCHES] loading A, A.tags, B", expectMatchingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityA.class, "tags")),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityB.class))
                ))),

                // NEGATIVE test cases
                dynamicTest("[NOT MATCHES] loading A, A, B", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityB.class))
                ))),
                dynamicTest("[NOT MATCHES] loading A.tags, A.users", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityA.class, "tags")),
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityA.class, "users"))
                )))
        );
    }

    @TestFactory
    Iterable<DynamicTest> shouldAssertNoImplicitOperationExceptAllOfMultipleWithTimesAtMostRule() {
        JPlusOneAssertionRule rule = JPlusOneAssertionRule
                .within().lastSession()
                .shouldBe().noImplicitOperations().exceptAllOf(exclusions -> exclusions
                        .loadingAnyEntity().times(atMost(2))
                        .loadingAnyCollection().times(atMost(1))
                );

        return asList(
                // POSITIVE test cases
                dynamicTest("[MATCHES] empty session", expectMatchingRule(rule, anySessionNode(
                ))),
                dynamicTest("[MATCHES] loading A.tags, A", expectMatchingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityA.class, "tags")),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class))
                ))),
                dynamicTest("[MATCHES] loading A, A.tags", expectMatchingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityA.class, "tags"))
                ))),
                dynamicTest("[MATCHES] loading A, A.tags, B", expectMatchingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityA.class, "tags")),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityB.class))
                ))),

                // NEGATIVE test cases
                dynamicTest("[NOT MATCHES] loading A", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class))
                ))),
                dynamicTest("[NOT MATCHES] loading A,B", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityB.class))
                ))),
                dynamicTest("[NOT MATCHES] loading A.tags", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityA.class, "tags"))
                ))),
                dynamicTest("[NOT MATCHES] loading A, A, B", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityB.class))
                ))),
                dynamicTest("[NOT MATCHES] loading A.tags, A.users", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityA.class, "tags")),
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityA.class, "users"))
                )))
        );
    }

    @TestFactory
    Iterable<DynamicTest> shouldAssertNoImplicitOperationExceptAllInOrderOfMultipleWithTimesAtMostRule() {
        JPlusOneAssertionRule rule = JPlusOneAssertionRule
                .within().lastSession()
                .shouldBe().noImplicitOperations().exceptAllOfInOrder(exclusions -> exclusions
                        .loadingAnyEntity().times(atMost(2))
                        .loadingAnyCollection().times(atMost(1))
                );

        return asList(
                // POSITIVE test cases
                dynamicTest("[MATCHES] empty session", expectMatchingRule(rule, anySessionNode(
                ))),
                dynamicTest("[MATCHES] loading A, A.tags", expectMatchingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityA.class, "tags"))
                ))),
                dynamicTest("[MATCHES] loading A, B, A.tags", expectMatchingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityB.class)),
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityA.class, "tags"))
                ))),

                // NEGATIVE test cases
                dynamicTest("[NOT MATCHES] loading A.tags, A", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityA.class, "tags")),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class))
                ))),
                dynamicTest("[NOT MATCHES] loading A, A.tags, B", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityA.class, "tags")),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityB.class))
                ))),
                dynamicTest("[NOT MATCHES] loading A", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class))
                ))),
                dynamicTest("[NOT MATCHES] loading A,B", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityB.class))
                ))),
                dynamicTest("[NOT MATCHES] loading A.tags", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityA.class, "tags"))
                ))),
                dynamicTest("[NOT MATCHES] loading A, A, B", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityB.class))
                ))),
                dynamicTest("[NOT MATCHES] loading A, B, A, A.tags", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityB.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityA.class, "tags"))
                ))),
                dynamicTest("[NOT MATCHES] loading A.tags, A.users", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityA.class, "tags")),
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityA.class, "users"))
                )))
        );
    }

    @TestFactory
    Iterable<DynamicTest> shouldAssertNoImplicitOperationExceptAnyOfMultipleWithTimesExactlyRule() {
        JPlusOneAssertionRule rule = JPlusOneAssertionRule
                .within().lastSession()
                .shouldBe().noImplicitOperations().exceptAnyOf(exclusions -> exclusions
                        .loadingAnyEntity().times(2)       // exactly
                        .loadingAnyCollection().times(1)
                );

        return asList(
                // POSITIVE test cases
                dynamicTest("[MATCHES] empty session", expectMatchingRule(rule, anySessionNode(
                ))),
                dynamicTest("[MATCHES] loading A,B", expectMatchingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityB.class))
                ))),
                dynamicTest("[MATCHES] loading A.tags", expectMatchingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityA.class, "tags"))
                ))),
                dynamicTest("[MATCHES] loading A, A.tags, B", expectMatchingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityA.class, "tags")),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityB.class))
                ))),

                // NEGATIVE test cases
                dynamicTest("[NOT MATCHES] loading A", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class))
                ))),
                dynamicTest("[NOT MATCHES] loading A, A.tags", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityA.class, "tags"))
                ))),
                dynamicTest("[NOT MATCHES] loading A, A, B", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityB.class))
                ))),
                dynamicTest("[NOT MATCHES] loading A.tags, A.users", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityA.class, "tags")),
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityA.class, "users"))
                )))
        );
    }

    @TestFactory
    Iterable<DynamicTest> shouldAssertNoImplicitOperationExceptAllOfMultipleWithTimesExactlyRule() {
        JPlusOneAssertionRule rule = JPlusOneAssertionRule
                .within().lastSession()
                .shouldBe().noImplicitOperations().exceptAllOf(exclusions -> exclusions
                        .loadingAnyEntity().times(2)
                        .loadingAnyCollection().times(1)
                );

        return asList(
                // POSITIVE test cases
                dynamicTest("[MATCHES] empty session", expectMatchingRule(rule, anySessionNode(
                ))),
                dynamicTest("[MATCHES] loading A, A.tags, B", expectMatchingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityA.class, "tags")),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityB.class))
                ))),

                // NEGATIVE test cases
                dynamicTest("[NOT MATCHES] loading A.tags, A", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityA.class, "tags")),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class))
                ))),
                dynamicTest("[NOT MATCHES] loading A, A.tags", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityA.class, "tags"))
                ))),
                dynamicTest("[NOT MATCHES] loading A", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class))
                ))),
                dynamicTest("[NOT MATCHES] loading A,B", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityB.class))
                ))),
                dynamicTest("[NOT MATCHES] loading A.tags", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityA.class, "tags"))
                ))),
                dynamicTest("[NOT MATCHES] loading A, A, B", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityB.class))
                ))),
                dynamicTest("[NOT MATCHES] loading A.tags, A.users", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityA.class, "tags")),
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityA.class, "users"))
                )))
        );
    }

    @TestFactory
    Iterable<DynamicTest> shouldAssertNoImplicitOperationExceptAllInOrderOfMultipleWithTimesExactlyRule() {
        JPlusOneAssertionRule rule = JPlusOneAssertionRule
                .within().lastSession()
                .shouldBe().noImplicitOperations().exceptAllOfInOrder(exclusions -> exclusions
                        .loadingAnyEntity().times(2)
                        .loadingAnyCollection().times(1)
                );

        return asList(
                // POSITIVE test cases
                dynamicTest("[MATCHES] empty session", expectMatchingRule(rule, anySessionNode(
                ))),
                dynamicTest("[MATCHES] loading A, B, A.tags", expectMatchingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityB.class)),
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityA.class, "tags"))
                ))),

                // NEGATIVE test cases
                dynamicTest("[NOT MATCHES] loading A, A.tags", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityA.class, "tags"))
                ))),
                dynamicTest("[NOT MATCHES] loading A.tags, A", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityA.class, "tags")),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class))
                ))),
                dynamicTest("[NOT MATCHES] loading A, A.tags, B", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityA.class, "tags")),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityB.class))
                ))),
                dynamicTest("[NOT MATCHES] loading A", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class))
                ))),
                dynamicTest("[NOT MATCHES] loading A,B", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityB.class))
                ))),
                dynamicTest("[NOT MATCHES] loading A.tags", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityA.class, "tags"))
                ))),
                dynamicTest("[NOT MATCHES] loading A, A, B", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityB.class))
                ))),
                dynamicTest("[NOT MATCHES] loading A, B, A, A.tags", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityB.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityA.class, "tags"))
                ))),
                dynamicTest("[NOT MATCHES] loading A.tags, A.users", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityA.class, "tags")),
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityA.class, "users"))
                )))
        );
    }

    @TestFactory
    Iterable<DynamicTest> shouldAssertNoImplicitOperationExceptAnyOfMultipleWithTimesAtLeastRule() {
        JPlusOneAssertionRule rule = JPlusOneAssertionRule
                .within().lastSession()
                .shouldBe().noImplicitOperations().exceptAnyOf(exclusions -> exclusions
                        .loadingAnyEntity().times(atLeast(2))
                        .loadingAnyCollection().times(atLeast(1))
                );

        return asList(
                // POSITIVE test cases
                dynamicTest("[MATCHES] empty session", expectMatchingRule(rule, anySessionNode(
                ))),

                dynamicTest("[MATCHES] loading A,B", expectMatchingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityB.class))
                ))),
                dynamicTest("[MATCHES] loading A.tags", expectMatchingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityA.class, "tags"))
                ))),

                dynamicTest("[MATCHES] loading A, A.tags, B", expectMatchingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityA.class, "tags")),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityB.class))
                ))),
                dynamicTest("[MATCHES] loading A.tags, A.users", expectMatchingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityA.class, "tags")),
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityA.class, "users"))
                ))),
                dynamicTest("[MATCHES] loading A, A, B", expectMatchingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityB.class))
                ))),

                // NEGATIVE test cases
                dynamicTest("[NOT MATCHES] loading A", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class))
                ))),
                dynamicTest("[NOT MATCHES] loading A, A.tags", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityA.class, "tags"))
                )))
        );
    }

    @TestFactory
    Iterable<DynamicTest> shouldAssertNoImplicitOperationExceptAllOfMultipleWithTimesAtLeastRule() {
        JPlusOneAssertionRule rule = JPlusOneAssertionRule
                .within().lastSession()
                .shouldBe().noImplicitOperations().exceptAllOf(exclusions -> exclusions
                        .loadingAnyEntity().times(atLeast(2))
                        .loadingAnyCollection().times(atLeast(1))
                );

        return asList(
                // POSITIVE test cases
                dynamicTest("[MATCHES] empty session", expectMatchingRule(rule, anySessionNode(
                ))),
                dynamicTest("[MATCHES] loading A, A.tags, A.users, B", expectMatchingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityA.class, "tags")),
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityA.class, "users")),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityB.class))
                ))),
                dynamicTest("[MATCHES] loading A, A.tags, B", expectMatchingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityA.class, "tags")),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityB.class))
                ))),
                dynamicTest("[MATCHES] loading A, B, A, A.tags", expectMatchingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityB.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityA.class, "tags"))
                ))),

                // NEGATIVE test cases
                dynamicTest("[NOT MATCHES] loading A.tags, A", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityA.class, "tags")),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class))
                ))),
                dynamicTest("[NOT MATCHES] loading A, A.tags", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityA.class, "tags"))
                ))),
                dynamicTest("[NOT MATCHES] loading A", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class))
                ))),
                dynamicTest("[NOT MATCHES] loading A,B", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityB.class))
                ))),
                dynamicTest("[NOT MATCHES] loading A.tags", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityA.class, "tags"))
                ))),
                dynamicTest("[NOT MATCHES] loading A, A, B", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityB.class))
                ))),
                dynamicTest("[NOT MATCHES] loading A.tags, A.users", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityA.class, "tags")),
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityA.class, "users"))
                )))
        );
    }

    @TestFactory
    Iterable<DynamicTest> shouldAssertNoImplicitOperationExceptAllInOrderOfMultipleWithTimesAtLeastRule() {
        JPlusOneAssertionRule rule = JPlusOneAssertionRule
                .within().lastSession()
                .shouldBe().noImplicitOperations().exceptAllOfInOrder(exclusions -> exclusions
                        .loadingAnyEntity().times(atLeast(2))
                        .loadingAnyCollection().times(atLeast(1))
                );

        return asList(
                // POSITIVE test cases
                dynamicTest("[MATCHES] empty session", expectMatchingRule(rule, anySessionNode(
                ))),

                dynamicTest("[MATCHES] loading A, B, A.tags", expectMatchingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityB.class)),
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityA.class, "tags"))
                ))),
                dynamicTest("[MATCHES] loading A, B, A, A.tags, A.users", expectMatchingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityB.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityA.class, "tags")),
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityA.class, "users"))
                ))),

                // NEGATIVE test cases
                dynamicTest("[NOT MATCHES] loading A, A.tags, A.users, B", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityA.class, "tags")),
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityA.class, "users")),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityB.class))
                ))),
                dynamicTest("[NOT MATCHES] loading A, A.tags, B", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityA.class, "tags")),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityB.class))
                ))),
                dynamicTest("[NOT MATCHES] loading A.tags, A", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityA.class, "tags")),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class))
                ))),
                dynamicTest("[NOT MATCHES] loading A, A.tags", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityA.class, "tags"))
                ))),
                dynamicTest("[NOT MATCHES] loading A", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class))
                ))),
                dynamicTest("[NOT MATCHES] loading A,B", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityB.class))
                ))),
                dynamicTest("[NOT MATCHES] loading A.tags", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityA.class, "tags"))
                ))),
                dynamicTest("[NOT MATCHES] loading A, A, B", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityB.class))
                ))),
                dynamicTest("[NOT MATCHES] loading A.tags, A.users", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityA.class, "tags")),
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityA.class, "users"))
                )))
        );
    }

    @TestFactory
    Iterable<DynamicTest> shouldAssertNoImplicitOperationExceptAllInOrderOfMultipleMixedRule() {
        JPlusOneAssertionRule rule = JPlusOneAssertionRule
                .within().lastSession()
                .shouldBe().noImplicitOperations().exceptAllOfInOrder(exclusions -> exclusions
                        .loadingEntity(SampleEntityA.class).times(atMost(2))
                        .loadingEntity(SampleEntityB.class).times(exactly(1))
                        .loadingCollection(SampleEntityB.class, "entries") // default is any positive number
                        .loadingEntity(SampleEntityA.class).times(1) // equivalent of exactly(1)
                );

        return asList(
                // POSITIVE test cases
                dynamicTest("[MATCHES] loading A, A, B, B.entries, A", expectMatchingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityB.class)),
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityB.class, "entries")),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class))
                ))),
                dynamicTest("[MATCHES] loading A, B, B.entries, A", expectMatchingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityB.class)),
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityB.class, "entries")),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class))
                ))),
                dynamicTest("[MATCHES] loading A, A, B, B.entries, B.entries, A", expectMatchingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityB.class)),
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityB.class, "entries")),
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityB.class, "entries")),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class))
                ))),

                // NEGATIVE test cases
                dynamicTest("[NOT MATCHES] loading B, B.entries, A", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityB.class)),
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityB.class, "entries")),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class))
                ))),
                dynamicTest("[NOT MATCHES] loading A, A, A, B, B.entries", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityB.class)),
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityB.class, "entries"))
                ))),
                dynamicTest("[NOT MATCHES] loading A, A, B, B.entries", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityB.class)),
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityB.class, "entries"))
                ))),
                dynamicTest("[NOT MATCHES] loading A, A, B, A", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityB.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class))
                ))),
                dynamicTest("[NOT MATCHES] loading A, A, B.entries, A", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityB.class, "entries")),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class))
                )))
        );
    }

    @TestFactory
    Iterable<DynamicTest> shouldAssertNoImplicitOperationExceptAllOfMultipleMixedRule() {
        JPlusOneAssertionRule rule = JPlusOneAssertionRule
                .within().lastSession()
                .shouldBe().noImplicitOperations().exceptAllOf(exclusions -> exclusions
                        .loadingEntity(SampleEntityA.class).times(atMost(3))
                        .loadingEntity(SampleEntityB.class).times(exactly(1))
                        .loadingCollection(SampleEntityB.class, "entries") // default is any positive number
                );

        return asList(
                // POSITIVE test cases
                dynamicTest("[MATCHES] loading A, A, B, B.entries, A", expectMatchingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityB.class)),
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityB.class, "entries")),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class))
                ))),
                dynamicTest("[MATCHES] loading A, B, B.entries, A", expectMatchingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityB.class)),
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityB.class, "entries")),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class))
                ))),
                dynamicTest("[MATCHES] loading A, A, B, B.entries, B.entries, A", expectMatchingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityB.class)),
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityB.class, "entries")),
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityB.class, "entries")),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class))
                ))),
                dynamicTest("[MATCHES] loading B, B.entries, A", expectMatchingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityB.class)),
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityB.class, "entries")),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class))
                ))),
                dynamicTest("[MATCHES] loading A, A, A, B, B.entries", expectMatchingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityB.class)),
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityB.class, "entries"))
                ))),
                dynamicTest("[MATCHES] loading A, A, B, B.entries", expectMatchingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityB.class)),
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityB.class, "entries"))
                ))),

                // NEGATIVE test cases
                dynamicTest("[NOT MATCHES] loading A, A, B, A", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityB.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class))
                ))),
                dynamicTest("[NOT MATCHES] loading A, A, B.entries, A", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityB.class, "entries")),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class))
                )))
        );
    }


}
