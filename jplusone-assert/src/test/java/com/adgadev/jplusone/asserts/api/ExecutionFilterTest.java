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
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.adgadev.jplusone.asserts.context.mother.FrameExtractMother.anyFrameExtract;
import static com.adgadev.jplusone.asserts.context.mother.FrameStackMother.anyFrameStack;
import static com.adgadev.jplusone.asserts.context.mother.OperationNodeMother.anyImplicitOperationNode;
import static com.adgadev.jplusone.asserts.context.mother.RootNodeMother.anyRootNode;
import static com.adgadev.jplusone.asserts.context.mother.SessionNodeMother.anySessionNode;
import static com.adgadev.jplusone.asserts.context.mother.SessionNodeMother.anySessionNodeWithImplicitOperation;
import static com.adgadev.jplusone.asserts.impl.JPlusOneAssertionContextMother.anyContext;
import static com.adgadev.jplusone.core.registry.LazyInitialisation.entityLazyInitialisation;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ExecutionFilterTest {

    @RequiredArgsConstructor
    private enum ContextDataSet {
        SESSION_WITH_DEFAULT_FRAME_STACK(anyContext(anyRootNode(
                anySessionNodeWithImplicitOperation()
        ))),
        SESSION_WITH_SAMPLE_SERVICE_A_IN_SESSION_FRAME_STACK(anyContext(anyRootNode(anySessionNode(
                anyImplicitOperationNode(),
                anyFrameStack(
                        anyFrameExtract(SampleServiceA.class, "methodA")
                )
        )))),
        SESSION_WITH_SAMPLE_SERVICE_AB_IN_SESSION_FRAME_STACK(anyContext(anyRootNode(anySessionNode(
                anyImplicitOperationNode(),
                anyFrameStack(asList(
                        anyFrameExtract(SampleServiceA.class, "methodA"),
                        anyFrameExtract(SampleServiceB.class, "methodB")
                ))
        )))),
        SESSION_WITH_SAMPLE_SERVICE_AB_IN_SESSION_FRAME_STACK_MIXED(anyContext(anyRootNode(anySessionNode(
                anyImplicitOperationNode(),
                anyFrameStack(asList(
                        anyFrameExtract(SampleServiceA.class, "methodA"),
                        anyFrameExtract(SampleServiceB.class, "methodB1"),
                        anyFrameExtract(SampleServiceA.class, "methodA"),
                        anyFrameExtract(SampleServiceB.class, "methodB2")
                ))
        )))),
        SESSION_WITH_SAMPLE_SERVICE_A_IN_OPERATION_FRAME_STACK(anyContext(anyRootNode(anySessionNode(
                anyImplicitOperationNode(
                        entityLazyInitialisation(SampleEntityA.class.getCanonicalName()),
                        anyFrameStack(
                                anyFrameExtract(SampleServiceA.class, "methodA")
                        )
                )
        )))),
        SESSION_WITH_SAMPLE_SERVICE_AB_IN_OPERATION_FRAME_STACK(anyContext(anyRootNode(anySessionNode(
                anyImplicitOperationNode(
                        entityLazyInitialisation(SampleEntityA.class.getCanonicalName()),
                        anyFrameStack(asList(
                                anyFrameExtract(SampleServiceA.class, "methodA"),
                                anyFrameExtract(SampleServiceB.class, "methodB")
                        ))
                )
        ))));

        private final JPlusOneAssertionContext context;
    }

    private static class SampleServiceA {
    }

    private static class SampleServiceB {
    }

    private static Stream<Arguments> provideAssertionWithInsideExecutionAnyClassMethodSucceedData() {
        return Stream.of(
                Arguments.of(ContextDataSet.SESSION_WITH_DEFAULT_FRAME_STACK, Integer.class),
                Arguments.of(ContextDataSet.SESSION_WITH_DEFAULT_FRAME_STACK, SampleServiceA.class),
                Arguments.of(ContextDataSet.SESSION_WITH_DEFAULT_FRAME_STACK, SampleServiceB.class),
                Arguments.of(ContextDataSet.SESSION_WITH_SAMPLE_SERVICE_A_IN_SESSION_FRAME_STACK, Integer.class),
                Arguments.of(ContextDataSet.SESSION_WITH_SAMPLE_SERVICE_A_IN_SESSION_FRAME_STACK, SampleServiceB.class)
        );
    }

    private static Stream<Arguments> provideAssertionWithInsideExecutionSpecificClassMethodSucceedData() {
        return Stream.of(
                Arguments.of(ContextDataSet.SESSION_WITH_DEFAULT_FRAME_STACK, Integer.class, "otherMethod"),
                Arguments.of(ContextDataSet.SESSION_WITH_DEFAULT_FRAME_STACK, SampleServiceA.class, "otherMethod"),
                Arguments.of(ContextDataSet.SESSION_WITH_DEFAULT_FRAME_STACK, SampleServiceB.class, "otherMethod"),

                Arguments.of(ContextDataSet.SESSION_WITH_SAMPLE_SERVICE_A_IN_SESSION_FRAME_STACK, Integer.class, "otherMethod"),
                Arguments.of(ContextDataSet.SESSION_WITH_SAMPLE_SERVICE_A_IN_SESSION_FRAME_STACK, SampleServiceB.class, "otherMethod"),
                Arguments.of(ContextDataSet.SESSION_WITH_SAMPLE_SERVICE_AB_IN_SESSION_FRAME_STACK, SampleServiceA.class, "otherMethod"),
                Arguments.of(ContextDataSet.SESSION_WITH_SAMPLE_SERVICE_AB_IN_SESSION_FRAME_STACK, SampleServiceB.class, "otherMethod"),
                Arguments.of(ContextDataSet.SESSION_WITH_SAMPLE_SERVICE_AB_IN_SESSION_FRAME_STACK_MIXED, SampleServiceA.class, "otherMethod"),
                Arguments.of(ContextDataSet.SESSION_WITH_SAMPLE_SERVICE_AB_IN_SESSION_FRAME_STACK_MIXED, SampleServiceB.class, "otherMethod"),

                Arguments.of(ContextDataSet.SESSION_WITH_SAMPLE_SERVICE_A_IN_OPERATION_FRAME_STACK, SampleServiceA.class, "otherMethod"),
                Arguments.of(ContextDataSet.SESSION_WITH_SAMPLE_SERVICE_AB_IN_OPERATION_FRAME_STACK, SampleServiceA.class, "otherMethod"),
                Arguments.of(ContextDataSet.SESSION_WITH_SAMPLE_SERVICE_AB_IN_OPERATION_FRAME_STACK, SampleServiceB.class, "otherMethod")
        );
    }

    private static Stream<Arguments> provideAssertionWithInsideExecutionAnyClassMethodFailData() {
        return Stream.of(
                Arguments.of(ContextDataSet.SESSION_WITH_SAMPLE_SERVICE_A_IN_SESSION_FRAME_STACK, SampleServiceA.class),
                Arguments.of(ContextDataSet.SESSION_WITH_SAMPLE_SERVICE_AB_IN_SESSION_FRAME_STACK, SampleServiceA.class),
                Arguments.of(ContextDataSet.SESSION_WITH_SAMPLE_SERVICE_AB_IN_SESSION_FRAME_STACK, SampleServiceB.class),
                Arguments.of(ContextDataSet.SESSION_WITH_SAMPLE_SERVICE_AB_IN_SESSION_FRAME_STACK_MIXED, SampleServiceA.class),
                Arguments.of(ContextDataSet.SESSION_WITH_SAMPLE_SERVICE_AB_IN_SESSION_FRAME_STACK_MIXED, SampleServiceB.class)
        );
    }

    private static Stream<Arguments> provideAssertionWithInsideExecutionSpecificClassMethodFailData() {
        return Stream.of(
                Arguments.of(ContextDataSet.SESSION_WITH_SAMPLE_SERVICE_A_IN_SESSION_FRAME_STACK, SampleServiceA.class, "methodA"),
                Arguments.of(ContextDataSet.SESSION_WITH_SAMPLE_SERVICE_AB_IN_SESSION_FRAME_STACK, SampleServiceA.class, "methodA"),
                Arguments.of(ContextDataSet.SESSION_WITH_SAMPLE_SERVICE_AB_IN_SESSION_FRAME_STACK, SampleServiceB.class, "methodB"),
                Arguments.of(ContextDataSet.SESSION_WITH_SAMPLE_SERVICE_AB_IN_SESSION_FRAME_STACK_MIXED, SampleServiceA.class, "methodA"),
                Arguments.of(ContextDataSet.SESSION_WITH_SAMPLE_SERVICE_AB_IN_SESSION_FRAME_STACK_MIXED, SampleServiceB.class, "methodB1"),
                Arguments.of(ContextDataSet.SESSION_WITH_SAMPLE_SERVICE_AB_IN_SESSION_FRAME_STACK_MIXED, SampleServiceB.class, "methodB2"),

                Arguments.of(ContextDataSet.SESSION_WITH_SAMPLE_SERVICE_A_IN_OPERATION_FRAME_STACK, SampleServiceA.class, "methodA"),
                Arguments.of(ContextDataSet.SESSION_WITH_SAMPLE_SERVICE_AB_IN_OPERATION_FRAME_STACK, SampleServiceA.class, "methodA"),
                Arguments.of(ContextDataSet.SESSION_WITH_SAMPLE_SERVICE_AB_IN_OPERATION_FRAME_STACK, SampleServiceB.class, "methodB")
        );
    }

    @ParameterizedTest
    @MethodSource("provideAssertionWithInsideExecutionAnyClassMethodSucceedData")
    void shouldAssertionWithInsideExecutionAnyClassMethodSucceed(ContextDataSet contextDataSet, Class<?> clazz) {
        JPlusOneAssertionRule rule = JPlusOneAssertionRule
                .within().lastSession().insideExecutionOfAnyMethodIn(clazz)
                .shouldBe().noImplicitOperations();

        rule.check(contextDataSet.context);
    }

    @ParameterizedTest
    @MethodSource("provideAssertionWithInsideExecutionSpecificClassMethodSucceedData")
    void shouldAssertionWithInsideExecutionSpecificClassMethodSucceed(ContextDataSet contextDataSet, Class<?> clazz, String methodName) {
        JPlusOneAssertionRule rule = JPlusOneAssertionRule
                .within().lastSession().insideExecutionOfMethod(clazz, methodName)
                .shouldBe().noImplicitOperations();

        rule.check(contextDataSet.context);
    }

    @ParameterizedTest
    @MethodSource("provideAssertionWithInsideExecutionAnyClassMethodFailData")
    void shouldAssertionWithInsideExecutionAnyClassMethodFail(ContextDataSet contextDataSet, Class<?> clazz) {
        JPlusOneAssertionRule rule = JPlusOneAssertionRule
                .within().lastSession().insideExecutionOfAnyMethodIn(clazz)
                .shouldBe().noImplicitOperations();

        Error error = assertThrows(AssertionError.class, () -> rule.check(contextDataSet.context));
        assertThat(error.getMessage(), startsWith("Actual amount of IMPLICIT operations after applying exclusions is different than the expected amount"));
    }

    @ParameterizedTest
    @MethodSource("provideAssertionWithInsideExecutionSpecificClassMethodFailData")
    void shouldAssertionWithInsideExecutionSpecificClassMethodFail(ContextDataSet contextDataSet, Class<?> clazz, String methodName) {
        JPlusOneAssertionRule rule = JPlusOneAssertionRule
                .within().lastSession().insideExecutionOfMethod(clazz, methodName)
                .shouldBe().noImplicitOperations();

        Error error = assertThrows(AssertionError.class, () -> rule.check(contextDataSet.context));
        assertThat(error.getMessage(), startsWith("Actual amount of IMPLICIT operations after applying exclusions is different than the expected amount"));
    }

    @Test
    void shouldAssertionWithInsideExecutionDecreaseAmountOfCapturedOperations() {
        JPlusOneAssertionContext context = anyContext(anyRootNode(anySessionNode(
                anyImplicitOperationNode(),
                anyImplicitOperationNode(
                        entityLazyInitialisation(SampleEntityA.class.getCanonicalName()),
                        anyFrameStack(
                                anyFrameExtract(SampleServiceA.class, "methodA")
                        )
                )
        )));

        JPlusOneAssertionRule rule = JPlusOneAssertionRule
                .within().lastSession().insideExecutionOfAnyMethodIn(SampleServiceA.class)
                .shouldBe().exactly(1).implicitOperations();

        rule.check(context);
    }
}