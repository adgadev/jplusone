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

import com.adgadev.jplusone.test.TestDomainApplication;
import com.adgadev.jplusone.test.domain.bookshop.Author;
import com.adgadev.jplusone.test.domain.bookshop.BookshopService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = TestDomainApplication.class)
class JPlusOneAssertionIntegrationTest {

    @Autowired
    private JPlusOneAssertionContext assertionContext;

    @Autowired
    private BookshopService bookshopService;

    @Test
    public void shouldCheckThatAssertionRuleForNoImplicitOperationExceptExclusionsHasBeenFollowed() {
        // given
        JPlusOneAssertionRule rule = JPlusOneAssertionRule
                .within().lastSession()
                .shouldBe().noImplicitOperations().exceptAnyOf(exclusions -> exclusions
                        .loadingEntity(Author.class)
                        .loadingCollection(Author.class, "books")
                );

        bookshopService.getSampleBookDetailsUsingLazyLoading();

        // expect
        rule.check(assertionContext);
    }

    @Test
    public void shouldCheckThatAssertionRuleForNoImplicitOperationsHasBeenBroken() {
        // given
        JPlusOneAssertionRule rule = JPlusOneAssertionRule
                .within().lastSession()
                .shouldBe().noImplicitOperations();

        bookshopService.getSampleBookDetailsUsingLazyLoading();

        // expect
        Error error = assertThrows(AssertionError.class, () -> rule.check(assertionContext));
        assertThat(error.getMessage(), startsWith("Actual amount of IMPLICIT operations after applying exclusions is different than the expected amount"));
    }

    @Test
    public void shouldCheckAmountOfSqlStatements() {
        // given
        JPlusOneAssertionRule rule = JPlusOneAssertionRule
                .within().lastSession()
                .shouldBe().atMost(3).sqlStatementsTotal();

        bookshopService.getSampleBookDetailsUsingLazyLoading();

        // expect
        rule.check(assertionContext);
    }
}