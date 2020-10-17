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
import static com.adgadev.jplusone.asserts.context.mother.LazyInitializationMother.anyCollectionLazyInitialisation;
import static com.adgadev.jplusone.asserts.context.mother.LazyInitializationMother.anyEntityLazyInitialisation;
import static com.adgadev.jplusone.asserts.context.mother.OperationNodeMother.anyExplicitOperationNode;
import static com.adgadev.jplusone.asserts.context.mother.OperationNodeMother.anyImplicitOperationNode;
import static com.adgadev.jplusone.asserts.context.mother.SessionNodeMother.anySessionNode;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

class ImplicitOperationSimpleRuleTest {

    @TestFactory
    Iterable<DynamicTest> shouldAssertNoImplicitOperationRule() {
        JPlusOneAssertionRule rule = JPlusOneAssertionRule
                .within().lastSession()
                .shouldBe().noImplicitOperations();

        return asList(
                // POSITIVE test cases
                dynamicTest("[MATCHES] Empty session", expectMatchingRule(rule, anySessionNode(
                        // empty
                ))),
                dynamicTest("[MATCHES] Explicit operation", expectMatchingRule(rule, anySessionNode(
                        anyExplicitOperationNode()
                ))),
                // NEGATIVE test cases
                dynamicTest("[NOT MATCHES] loading entity", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class))
                ))),
                dynamicTest("[NOT MATCHES] loading collection", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityA.class, "tags"))
                )))
        );
    }

    @TestFactory
    Iterable<DynamicTest> shouldAssertNoImplicitOperationExceptLoadingAnyEntityRule() {
        JPlusOneAssertionRule rule = JPlusOneAssertionRule
                .within().lastSession()
                .shouldBe().noImplicitOperations().exceptLoadingAnyEntity();

        return asList(
                // POSITIVE test cases
                dynamicTest("[MATCHES] Empty session", expectMatchingRule(rule, anySessionNode(
                        // empty
                ))),
                dynamicTest("[MATCHES] Explicit operation", expectMatchingRule(rule, anySessionNode(
                        anyExplicitOperationNode()
                ))),
                dynamicTest("[MATCHES] loading entity", expectMatchingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class))
                ))),
                // NEGATIVE test cases
                dynamicTest("[NOT MATCHES] loading collection", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityA.class, "tags"))
                ))),
                dynamicTest("[NOT MATCHES] loading entity & collection", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityA.class, "tags"))
                )))
        );
    }

    @TestFactory
    Iterable<DynamicTest> shouldAssertNoImplicitOperationExceptLoadingEntityARule() {
        JPlusOneAssertionRule rule = JPlusOneAssertionRule
                .within().lastSession()
                .shouldBe().noImplicitOperations().exceptLoadingEntity(SampleEntityA.class);

        return asList(
                // POSITIVE test cases
                dynamicTest("[MATCHES] Empty session", expectMatchingRule(rule, anySessionNode(
                        // empty
                ))),
                dynamicTest("[MATCHES] Explicit operation", expectMatchingRule(rule, anySessionNode(
                        anyExplicitOperationNode()
                ))),
                dynamicTest("[MATCHES] loading entity A", expectMatchingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class))
                ))),
                // NEGATIVE test cases
                dynamicTest("[NOT MATCHES] loading entity B", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityB.class))
                ))),
                dynamicTest("[NOT MATCHES] loading collection", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityA.class, "tags"))
                ))),
                dynamicTest("Implicit operation - loading entity A & B", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityB.class))
                )))
        );
    }

    @TestFactory
    Iterable<DynamicTest> shouldAssertNoImplicitOperationExceptLoadingAnyCollectionRule() {
        JPlusOneAssertionRule rule = JPlusOneAssertionRule
                .within().lastSession()
                .shouldBe().noImplicitOperations().exceptLoadingAnyCollection();

        return asList(
                // POSITIVE test cases
                dynamicTest("[MATCHES] Empty session", expectMatchingRule(rule, anySessionNode(
                        // empty
                ))),
                dynamicTest("[MATCHES] Explicit operation", expectMatchingRule(rule, anySessionNode(
                        anyExplicitOperationNode()
                ))),
                dynamicTest("[MATCHES] loading collection", expectMatchingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityA.class, "tags"))
                ))),
                // NEGATIVE test cases
                dynamicTest("[NOT MATCHES] loading entity", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class))
                ))),
                dynamicTest("[NOT MATCHES] loading entity & collection", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityA.class, "tags"))
                )))
        );
    }

    @TestFactory
    Iterable<DynamicTest> shouldAssertNoImplicitOperationExceptLoadingCollectionATagsRule() {
        JPlusOneAssertionRule rule = JPlusOneAssertionRule
                .within().lastSession()
                .shouldBe().noImplicitOperations().exceptLoadingCollection(SampleEntityA.class, "tags");

        return asList(
                // POSITIVE test cases
                dynamicTest("[MATCHES] Empty session", expectMatchingRule(rule, anySessionNode(
                        // empty
                ))),
                dynamicTest("[MATCHES] Explicit operation", expectMatchingRule(rule, anySessionNode(
                        anyExplicitOperationNode()
                ))),
                dynamicTest("[MATCHES] loading collection A.tags", expectMatchingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityA.class, "tags"))
                ))),
                // NEGATIVE test cases
                dynamicTest("[NOT MATCHES] loading entity", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class))
                ))),
                dynamicTest("[NOT MATCHES] loading collection A.other", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityA.class, "other"))
                ))),
                dynamicTest("[NOT MATCHES] loading collection B.tags", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityB.class, "tags"))
                )))
        );
    }

}