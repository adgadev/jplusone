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
import com.adgadev.jplusone.test.domain.bookshop.Author;
import com.adgadev.jplusone.test.domain.bookshop.Book;
import com.adgadev.jplusone.test.domain.bookshop.BookRepository;
import com.adgadev.jplusone.test.domain.bookshop.BookshopService;
import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityManager;

import static com.adgadev.jplusone.asserts.api.builder.AmountMatcher.*;
import static com.adgadev.jplusone.asserts.api.builder.SqlStatementType.INSERT_STATEMENT;
import static com.adgadev.jplusone.asserts.api.builder.SqlStatementType.SELECT_STATEMENT;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

class JPlusOneAssertionRuleTest {

    @Test
    void shouldCreateRule1() {
        // strict checking on all
        JPlusOneAssertionRule rule = JPlusOneAssertionRule
                .within().lastSession()
                .shouldBe().noImplicitOperations();
        assertThat(rule, notNullValue());
    }

    @Test
    void shouldCreateRule2() {
        // heuristic for detecting tests in which it's worth to reduce lazy loading
        JPlusOneAssertionRule rule = JPlusOneAssertionRule
                .within().lastSession()
                .shouldBe().atMost(2).implicitOperations();
        assertThat(rule, notNullValue());
    }

    @Test
    void shouldCreateRule3() {
        // ensuring no more implicit operations in specific test case appeared
        JPlusOneAssertionRule rule = JPlusOneAssertionRule
                .within().eachSession()
                .shouldBe().noImplicitOperations().exceptLoadingEntity(Book.class);
        assertThat(rule, notNullValue());
    }

    @Test
    void shouldCreateRule4() {
        // ensuring no more implicit operations in specific test case appeared
        JPlusOneAssertionRule rule = JPlusOneAssertionRule
                .within().lastSession()
                .shouldBe().noImplicitOperations().exceptAnyOf(builder -> builder
                        .loadingEntity(Book.class)
                        .loadingEntity(Author.class)
                        .loadingAnyCollectionInEntity(Book.class)
                );
        assertThat(rule, notNullValue());
    }

    @Test
    void shouldCreateRule4b() {
        // ensuring no more implicit operations in specific test case appeared
        JPlusOneAssertionRule rule = JPlusOneAssertionRule
                .within().lastSession()
                .shouldBe().noImplicitOperations().exceptAnyOf(builder -> builder
                        .loadingEntity(Book.class).times(atMost(2))
                        .loadingEntity(Author.class)
                        .loadingAnyCollectionInEntity(Book.class).times(exactly(1))
                        .loadingCollection(Author.class, "books").times(atLeast(2))

                );
        assertThat(rule, notNullValue());
    }

    @Test
    void shouldCreateRule5() {
        // ensuring no more implicit operations in specific test case appeared
        JPlusOneAssertionRule rule = JPlusOneAssertionRule
                .within().lastSession()
                .shouldBe().noImplicitOperations().exceptAnyOf(builder -> builder
                        .loadingAnyEntity().times(2)
                        .loadingAnyCollection().times(3)
                );
        assertThat(rule, notNullValue());
    }

    @Test
    void shouldCreateRule6() {
        // ensuring no more implicit operations in specific test case appeared
        JPlusOneAssertionRule rule = JPlusOneAssertionRule
                .within().lastSession()
                .shouldBe().noImplicitOperations().exceptAllOf(builder -> builder
                        .loadingEntity(Author.class)
                        .loadingAnyCollectionInEntity(Author.class).times(atMost(2))
                );
        assertThat(rule, notNullValue());
    }

    @Test
    void shouldCreateRule7() {
        // ensuring no more implicit operations in specific test case appeared
        JPlusOneAssertionRule rule = JPlusOneAssertionRule
                .within().lastSession()
                .shouldBe().noImplicitOperations().exceptAllOfInOrder(builder -> builder
                        .loadingEntity(Author.class)
                        .loadingEntity(Book.class).times(atMost(2))
                        .loadingEntity(Author.class)
                );
        assertThat(rule, notNullValue());
    }

    @Test
    void shouldCreateRule8() {
        //SQL statistics (implicit & explicit)
        JPlusOneAssertionRule rule = JPlusOneAssertionRule
                .within().lastSession()
                .shouldBe().atMost(10).sqlStatementsTotal(SELECT_STATEMENT)
                .andShouldBe().atMost(2).sqlStatementsTotal(INSERT_STATEMENT)
                .andShouldBe().none().sqlStatementsTotal(SqlStatementGroupType.OTHER_STATEMENTS);
        assertThat(rule, notNullValue());
    }

    @Test
    void shouldCreateRule9() {
        JPlusOneAssertionRule rule = JPlusOneAssertionRule
                .within().lastSession()
                .shouldBe().noExplicitOperations();
        assertThat(rule, notNullValue());
    }

    @Test
    void shouldCreateRule10() {
        // ensuring that explicit operations are used only for fetching
        JPlusOneAssertionRule rule = JPlusOneAssertionRule
                .within().lastSession()
                .shouldBe().noExplicitOperations().exceptFetchingData();
        assertThat(rule, notNullValue());
    }

    @Test
    void shouldCreateRule11() {
        JPlusOneAssertionRule rule = JPlusOneAssertionRule
                .within().lastSession()
                .shouldBe().noExplicitOperations().exceptFetchingDataVia(BookRepository.class, "findById");
        assertThat(rule, notNullValue());
    }

    @Test
    void shouldCreateRule12() {
        JPlusOneAssertionRule rule = JPlusOneAssertionRule
                .within().lastSession()
                .shouldBe().noExplicitOperations().exceptAnyOf(exclusions -> exclusions
                        .fetchingDataViaAnyMethodIn(BookRepository.class).times(atMost(3))
                        .fetchingDataVia(EntityManager.class, "find"));
        assertThat(rule, notNullValue());
    }

    @Test
    void shouldCreateRule13() {
        JPlusOneAssertionRule rule = JPlusOneAssertionRule
                .within().lastSession().insideExecutionOfAnyMethodIn(BookshopService.class)
                .shouldBe().noImplicitOperations();
        assertThat(rule, notNullValue());
    }

    @Test
    void shouldCreateRule14() {
        JPlusOneAssertionRule rule = JPlusOneAssertionRule
                .within().lastSession().insideExecutionOfAnyMethodIn(BookshopService.class)
                .shouldBe().noImplicitOperations().exceptAnyOf(builder -> builder
                        .loadingAnyEntity().times(2)
                        .loadingAnyCollection().times(3))
                .andShouldBe().noExplicitOperations().exceptFetchingDataViaAnyMethodIn(EntityManager.class)
                .andShouldBe().atMost(10).sqlStatementsTotal(SELECT_STATEMENT);
        assertThat(rule, notNullValue());
    }
}