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

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.adgadev.jplusone.asserts.context.mother.RootNodeMother.anyRootNode;
import static com.adgadev.jplusone.asserts.context.mother.SessionNodeMother.anySessionNodeWithExplicitOperation;
import static com.adgadev.jplusone.asserts.context.mother.SessionNodeMother.anySessionNodeWithImplicitOperation;
import static com.adgadev.jplusone.asserts.impl.JPlusOneAssertionContextMother.anyContext;
import static java.util.Collections.emptyList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SessionSelectorRuleTest {

    @RequiredArgsConstructor
    private enum ContextDataSet {
        NO_SESSION(anyContext(anyRootNode(
                emptyList()
        ))),
        SESSION_WITH_IMPLICIT_OPERATION(anyContext(anyRootNode(
                anySessionNodeWithImplicitOperation()
        ))),
        SESSION_WITH_EXPLICIT_OPERATION(anyContext(anyRootNode(
                anySessionNodeWithExplicitOperation()
        ))),
        TWO_SESSIONS_WITH_IMPLICIT_OPERATIONS(anyContext(anyRootNode(
                anySessionNodeWithImplicitOperation(),
                anySessionNodeWithImplicitOperation()
        ))),
        TWO_SESSIONS_WITH_EXPLICIT_OPERATIONS(anyContext(anyRootNode(
                anySessionNodeWithExplicitOperation(),
                anySessionNodeWithExplicitOperation()
        ))),
        SESSION_WITH_IMPLICIT_OPERATION_AND_SESSION_WITH_EXPLICIT_OPERATION(anyContext(anyRootNode(
                anySessionNodeWithImplicitOperation(),
                anySessionNodeWithExplicitOperation()
        ))),
        SESSION_WITH_EXPLICIT_OPERATION_AND_SESSION_WITH_IMPLICIT_OPERATION(anyContext(anyRootNode(
                anySessionNodeWithExplicitOperation(),
                anySessionNodeWithImplicitOperation()
        )));

        private final JPlusOneAssertionContext context;
    }

    @ParameterizedTest
    @EnumSource(value = ContextDataSet.class, names = {
            "NO_SESSION",
            "SESSION_WITH_EXPLICIT_OPERATION",
            "TWO_SESSIONS_WITH_EXPLICIT_OPERATIONS",
            "SESSION_WITH_IMPLICIT_OPERATION_AND_SESSION_WITH_EXPLICIT_OPERATION"
    })
    void shouldLastSessionAssertionSucceed(ContextDataSet contextDataSet) {
        JPlusOneAssertionRule rule = JPlusOneAssertionRule
                .within().lastSession()
                .shouldBe().noImplicitOperations();

        rule.check(contextDataSet.context);
    }

    @ParameterizedTest
    @EnumSource(value = ContextDataSet.class, names = {
            "SESSION_WITH_IMPLICIT_OPERATION",
            "TWO_SESSIONS_WITH_IMPLICIT_OPERATIONS",
            "SESSION_WITH_EXPLICIT_OPERATION_AND_SESSION_WITH_IMPLICIT_OPERATION"
    })
    void shouldLastSessionAssertionFail(ContextDataSet contextDataSet) {
        JPlusOneAssertionRule rule = JPlusOneAssertionRule
                .within().lastSession()
                .shouldBe().noImplicitOperations();

        Error error = assertThrows(AssertionError.class, () -> rule.check(contextDataSet.context));
        assertThat(error.getMessage(), startsWith("Actual amount of IMPLICIT operations after applying exclusions is different than the expected amount"));
    }

    @ParameterizedTest
    @EnumSource(value = ContextDataSet.class, names = {
            "NO_SESSION",
            "SESSION_WITH_EXPLICIT_OPERATION",
            "TWO_SESSIONS_WITH_EXPLICIT_OPERATIONS",
            "SESSION_WITH_EXPLICIT_OPERATION_AND_SESSION_WITH_IMPLICIT_OPERATION"

    })
    void shouldFirstSessionAssertionSucceed(ContextDataSet contextDataSet) {
        JPlusOneAssertionRule rule = JPlusOneAssertionRule
                .within().firstSession()
                .shouldBe().noImplicitOperations();

        rule.check(contextDataSet.context);
    }

    @ParameterizedTest
    @EnumSource(value = ContextDataSet.class, names = {
            "SESSION_WITH_IMPLICIT_OPERATION",
            "TWO_SESSIONS_WITH_IMPLICIT_OPERATIONS",
            "SESSION_WITH_IMPLICIT_OPERATION_AND_SESSION_WITH_EXPLICIT_OPERATION"
    })
    void shouldFirstSessionAssertionFail(ContextDataSet contextDataSet) {
        JPlusOneAssertionRule rule = JPlusOneAssertionRule
                .within().firstSession()
                .shouldBe().noImplicitOperations();

        Error error = assertThrows(AssertionError.class, () -> rule.check(contextDataSet.context));
        assertThat(error.getMessage(), startsWith("Actual amount of IMPLICIT operations after applying exclusions is different than the expected amount"));
    }

    @ParameterizedTest
    @EnumSource(value = ContextDataSet.class, names = {
            "NO_SESSION",
            "SESSION_WITH_EXPLICIT_OPERATION",
            "TWO_SESSIONS_WITH_EXPLICIT_OPERATIONS",
    })
    void shouldEachSessionAssertionSucceed(ContextDataSet contextDataSet) {
        JPlusOneAssertionRule rule = JPlusOneAssertionRule
                .within().eachSession()
                .shouldBe().noImplicitOperations();

        rule.check(contextDataSet.context);
    }

    @ParameterizedTest
    @EnumSource(value = ContextDataSet.class, names = {
            "SESSION_WITH_IMPLICIT_OPERATION",
            "TWO_SESSIONS_WITH_IMPLICIT_OPERATIONS",
            "SESSION_WITH_IMPLICIT_OPERATION_AND_SESSION_WITH_EXPLICIT_OPERATION",
            "SESSION_WITH_EXPLICIT_OPERATION_AND_SESSION_WITH_IMPLICIT_OPERATION"
    })
    void shouldEachSessionAssertionFail(ContextDataSet contextDataSet) {
        JPlusOneAssertionRule rule = JPlusOneAssertionRule
                .within().eachSession()
                .shouldBe().noImplicitOperations();

        Error error = assertThrows(AssertionError.class, () -> rule.check(contextDataSet.context));
        assertThat(error.getMessage(), startsWith("Actual amount of IMPLICIT operations after applying exclusions is different than the expected amount"));
    }

    private static Stream<Arguments> provideNthSessionAssertionSucceedData() {
        return Stream.of(
                Arguments.of(ContextDataSet.SESSION_WITH_EXPLICIT_OPERATION, 0),
                Arguments.of(ContextDataSet.TWO_SESSIONS_WITH_EXPLICIT_OPERATIONS, 0),
                Arguments.of(ContextDataSet.TWO_SESSIONS_WITH_EXPLICIT_OPERATIONS, 1),
                Arguments.of(ContextDataSet.SESSION_WITH_EXPLICIT_OPERATION_AND_SESSION_WITH_IMPLICIT_OPERATION, 0),
                Arguments.of(ContextDataSet.SESSION_WITH_IMPLICIT_OPERATION_AND_SESSION_WITH_EXPLICIT_OPERATION, 1)
        );
    }

    @ParameterizedTest
    @MethodSource("provideNthSessionAssertionSucceedData")
    void shouldNthSessionAssertionSucceed(ContextDataSet contextDataSet, int sessionNumber) {
        JPlusOneAssertionRule rule = JPlusOneAssertionRule
                .within().nthSession(sessionNumber)
                .shouldBe().noImplicitOperations();

        rule.check(contextDataSet.context);
    }

    private static Stream<Arguments> provideNthSessionAssertionFailData() {
        return Stream.of(
                Arguments.of(ContextDataSet.SESSION_WITH_IMPLICIT_OPERATION, 0),
                Arguments.of(ContextDataSet.TWO_SESSIONS_WITH_IMPLICIT_OPERATIONS, 0),
                Arguments.of(ContextDataSet.TWO_SESSIONS_WITH_IMPLICIT_OPERATIONS, 1),
                Arguments.of(ContextDataSet.SESSION_WITH_EXPLICIT_OPERATION_AND_SESSION_WITH_IMPLICIT_OPERATION, 1),
                Arguments.of(ContextDataSet.SESSION_WITH_IMPLICIT_OPERATION_AND_SESSION_WITH_EXPLICIT_OPERATION, 0)
        );
    }

    @ParameterizedTest
    @MethodSource("provideNthSessionAssertionFailData")
    void shouldNthSessionAssertionFail(ContextDataSet contextDataSet, int sessionNumber) {
        JPlusOneAssertionRule rule = JPlusOneAssertionRule
                .within().nthSession(sessionNumber)
                .shouldBe().noImplicitOperations();

        Error error = assertThrows(AssertionError.class, () -> rule.check(contextDataSet.context));
        assertThat(error.getMessage(), startsWith("Actual amount of IMPLICIT operations after applying exclusions is different than the expected amount"));
    }

    private static Stream<Arguments> provideNthSessionInvalidData() {
        return Stream.of(
                Arguments.of(ContextDataSet.SESSION_WITH_EXPLICIT_OPERATION, 1, RuntimeException.class, "Session number is larger than last captured session number"),
                Arguments.of(ContextDataSet.NO_SESSION, 0, RuntimeException.class, "Session number is larger than last captured session number")
        );
    }

    @ParameterizedTest
    @MethodSource("provideNthSessionInvalidData")
    void shouldNthSessionAssertionFailWithRuntimeException(ContextDataSet contextDataSet, int sessionNumber, Class<? extends Exception> exceptionClass, String exceptionMessage) {
        JPlusOneAssertionRule rule = JPlusOneAssertionRule
                .within().nthSession(sessionNumber)
                .shouldBe().noImplicitOperations();

        Exception exception = assertThrows(exceptionClass, () -> rule.check(contextDataSet.context));
        assertThat(exception.getMessage(), equalTo(exceptionMessage));
    }

    @Test
    void shouldFailToCreateNthSessionRuleForNegativeNumber() {
        assertThrows(RuntimeException.class, () -> JPlusOneAssertionRule
                .within().nthSession(-1)
                .shouldBe().noImplicitOperations());
    }
}