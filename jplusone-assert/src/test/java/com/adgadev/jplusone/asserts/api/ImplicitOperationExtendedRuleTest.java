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
import com.adgadev.jplusone.asserts.context.mother.OperationNodeMother.SampleEntityC;
import com.adgadev.jplusone.asserts.context.stub.SessionNodeStub;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.function.Executable;

import static com.adgadev.jplusone.asserts.context.mother.LazyInitializationMother.anyCollectionLazyInitialisation;
import static com.adgadev.jplusone.asserts.context.mother.LazyInitializationMother.anyEntityLazyInitialisation;
import static com.adgadev.jplusone.asserts.context.mother.OperationNodeMother.anyExplicitOperationNode;
import static com.adgadev.jplusone.asserts.context.mother.OperationNodeMother.anyImplicitOperationNode;
import static com.adgadev.jplusone.asserts.context.mother.RootNodeMother.anyRootNode;
import static com.adgadev.jplusone.asserts.context.mother.SessionNodeMother.anySessionNode;
import static com.adgadev.jplusone.asserts.impl.JPlusOneAssertionContextMother.anyContext;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

class ImplicitOperationExtendedRuleTest {

    @TestFactory
    Iterable<DynamicTest> shouldAssertNoImplicitOperationExceptAnyOfLoadingAnyEntityRule() {
        JPlusOneAssertionRule rule = JPlusOneAssertionRule
                .within().lastSession()
                .shouldBe().noImplicitOperations().exceptAnyOf(exclusions -> exclusions
                        .loadingAnyEntity()
                );

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
    Iterable<DynamicTest> shouldAssertNoImplicitOperationExceptAnyOfLoadingEntityARule() {
        JPlusOneAssertionRule rule = JPlusOneAssertionRule
                .within().lastSession()
                .shouldBe().noImplicitOperations().exceptAnyOf(exclusions -> exclusions
                        .loadingEntity(SampleEntityA.class)
                );

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
                dynamicTest("[NOT MATCHES] loading entity A & B", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityB.class))
                )))
        );
    }

    @TestFactory
    Iterable<DynamicTest> shouldAssertNoImplicitOperationExceptAnyOfLoadingAnyCollectionRule() {
        JPlusOneAssertionRule rule = JPlusOneAssertionRule
                .within().lastSession()
                .shouldBe().noImplicitOperations().exceptAnyOf(exclusions -> exclusions
                        .loadingAnyCollection()
                );

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
    Iterable<DynamicTest> shouldAssertNoImplicitOperationExceptAnyOfLoadingAnyCollectionInClassARule() {
        JPlusOneAssertionRule rule = JPlusOneAssertionRule
                .within().lastSession()
                .shouldBe().noImplicitOperations().exceptAnyOf(exclusions -> exclusions
                        .loadingAnyCollectionInEntity(SampleEntityA.class)
                );

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
                dynamicTest("[NOT MATCHES] loading collection A.other", expectMatchingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityA.class, "other"))
                ))),
                // NEGATIVE test cases
                dynamicTest("[NOT MATCHES] loading entity", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class))
                ))),
                dynamicTest("[NOT MATCHES] loading collection B.tags", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityB.class, "tags"))
                )))
        );
    }

    @TestFactory
    Iterable<DynamicTest> shouldAssertNoImplicitOperationExceptAnyOfLoadingCollectionATagsRule() {
        JPlusOneAssertionRule rule = JPlusOneAssertionRule
                .within().lastSession()
                .shouldBe().noImplicitOperations().exceptAnyOf(exclusions -> exclusions
                        .loadingCollection(SampleEntityA.class, "tags")
                );

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

    @TestFactory
    Iterable<DynamicTest> shouldAssertNoImplicitOperationExceptAnyOfMultipleSpecifiedRule() {
        JPlusOneAssertionRule rule = JPlusOneAssertionRule
                .within().lastSession()
                .shouldBe().noImplicitOperations().exceptAnyOf(exclusions -> exclusions
                        .loadingEntity(SampleEntityA.class)
                        .loadingEntity(SampleEntityB.class)
                );

        return asList(
                // POSITIVE test cases
                dynamicTest("[MATCHES] loading entity A", expectMatchingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class))
                ))),
                dynamicTest("[MATCHES] loading entity B", expectMatchingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityB.class))
                ))),
                dynamicTest("[MATCHES] loading entity A and then B", expectMatchingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityB.class))
                ))),
                dynamicTest("[MATCHES] loading entity B and then A", expectMatchingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityB.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class))
                ))),
                dynamicTest("[MATCHES] loading entity A, B, A, A", expectMatchingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityB.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class))
                ))),

                // NEGATIVE test cases
                dynamicTest("[NOT MATCHES] loading entity C", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityC.class))
                ))),
                dynamicTest("[NOT MATCHES] loading entity A, B, C", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityB.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityC.class))
                )))
        );
    }

    @TestFactory
    Iterable<DynamicTest> shouldAssertNoImplicitOperationExceptAllOfMultipleSpecifiedRule() {
        JPlusOneAssertionRule rule = JPlusOneAssertionRule
                .within().lastSession()
                .shouldBe().noImplicitOperations().exceptAllOf(exclusions -> exclusions
                        .loadingEntity(SampleEntityA.class)
                        .loadingEntity(SampleEntityB.class)
                );

        return asList(
                // POSITIVE test cases
                dynamicTest("[MATCHES] loading entity A and then B", expectMatchingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityB.class))
                ))),
                dynamicTest("[MATCHES] loading entity B and then A", expectMatchingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityB.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class))
                ))),
                dynamicTest("[MATCHES] loading entity A, B, A, A", expectMatchingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityB.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class))
                ))),

                // NEGATIVE test cases
                dynamicTest("[NOT MATCHES] loading entity A", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class))
                ))),
                dynamicTest("[NOT MATCHES] loading entity B", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityB.class))
                ))),
                dynamicTest("[NOT MATCHES] loading entity C", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityC.class))
                ))),
                dynamicTest("[NOT MATCHES] loading entity A, B, C", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityB.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityC.class))
                )))
        );
    }

    @TestFactory
    Iterable<DynamicTest> shouldAssertNoImplicitOperationExceptAllOfInOrderMultipleSpecifiedRule() {
        JPlusOneAssertionRule rule = JPlusOneAssertionRule
                .within().lastSession()
                .shouldBe().noImplicitOperations().exceptAllOfInOrder(exclusions -> exclusions
                        .loadingEntity(SampleEntityA.class)
                        .loadingEntity(SampleEntityB.class)
                );

        return asList(
                // POSITIVE test cases
                dynamicTest("[MATCHES] loading entity A and then B", expectMatchingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityB.class))
                ))),
                dynamicTest("[MATCHES] loading entity A, A, B, B", expectMatchingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityB.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityB.class))
                ))),

                // NEGATIVE test cases
                dynamicTest("[NOT MATCHES] loading entity A", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class))
                ))),
                dynamicTest("[NOT MATCHES] loading entity B", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityB.class))
                ))),
                dynamicTest("[NOT MATCHES] loading entity B and then A", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityB.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class))
                ))),
                dynamicTest("[NOT MATCHES] loading entity A, B, A, A", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityB.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class))
                ))),
                dynamicTest("[NOT MATCHES] loading entity C", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityC.class))
                ))),
                dynamicTest("[NOT MATCHES] loading entity A, B, C", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityB.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityC.class))
                )))
        );
    }

    @TestFactory
    Iterable<DynamicTest> shouldAssertNoImplicitOperationExceptAnyOfThreeSpecifiedRule() {
        JPlusOneAssertionRule rule = JPlusOneAssertionRule
                .within().lastSession()
                .shouldBe().noImplicitOperations().exceptAnyOf(exclusions -> exclusions
                        .loadingEntity(SampleEntityA.class)
                        .loadingEntity(SampleEntityB.class)
                        .loadingCollection(SampleEntityA.class, "tags")
                );

        return asList(
                // POSITIVE test cases
                dynamicTest("[MATCHES] loading entity A and then B, then A.tags", expectMatchingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityB.class)),
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityA.class, "tags"))
                ))),
                dynamicTest("[MATCHES] loading collection A.tags and then entity A and then B", expectMatchingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityA.class, "tags")),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityB.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class))
                ))),

                dynamicTest("[MATCHES] loading entity A", expectMatchingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class))
                ))),
                dynamicTest("[MATCHES] loading entity B", expectMatchingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityB.class))
                ))),
                dynamicTest("[MATCHES] loading collection", expectMatchingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityA.class, "tags"))
                ))),
                dynamicTest("[MATCHES] loading entity A and then B", expectMatchingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityB.class))
                ))),
                dynamicTest("[MATCHES] loading entity B and then A", expectMatchingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityB.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class))
                ))),

                dynamicTest("[MATCHES] loading entity A, B, A, A", expectMatchingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityB.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class))
                ))),
                dynamicTest("[MATCHES] loading collection A.tags twice and then ", expectMatchingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityA.class, "tags")),
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityA.class, "tags")),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class))
                ))),

                // NEGATIVE test cases
                dynamicTest("[NOT MATCHES] loading entity C", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityC.class))
                ))),
                dynamicTest("[NOT MATCHES] loading entity A, B, C", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityA.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityB.class)),
                        anyImplicitOperationNode(anyEntityLazyInitialisation(SampleEntityC.class))
                ))),
                dynamicTest("[NOT MATCHES] loading collection A.other", expectFailingRule(rule, anySessionNode(
                        anyImplicitOperationNode(anyCollectionLazyInitialisation(SampleEntityA.class, "other"))
                )))
        );
    }

    private Executable expectMatchingRule(JPlusOneAssertionRule rule, SessionNodeStub session) {
        return () -> rule.check(anyContext(anyRootNode(session)));
    }

    private Executable expectFailingRule(JPlusOneAssertionRule rule, SessionNodeStub session) {
        return () -> assertThrows(AssertionError.class, () ->
                rule.check(anyContext(anyRootNode(session)))
        );
    }

    // TODO: add tests for rules using .times()

}