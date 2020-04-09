package com.grexdev.nplusone.asserts.api;

import static com.grexdev.nplusone.asserts.api.builder.AmountMatcher.atMost;
import static com.grexdev.nplusone.asserts.api.builder.AmountMatcher.exactly;
import static com.grexdev.nplusone.asserts.api.builder.SqlStatementGroupType.OTHER_STATEMENTS;
import static com.grexdev.nplusone.asserts.api.builder.SqlStatementType.*;

class NPlusOneAssertionsTest {

    void test(NPlusOneAssertions assertions) {
        // TODO: API use cases

        // 1. strict checking on all
        NPlusOneAssertionRule rule1 = assertions
                .within().lastSession()
                .shouldBe().noImplicitOperations();

        // 2. strict checking without flush
        NPlusOneAssertionRule rule2 = assertions
                .within().lastSession()
                .shouldBe().noImplicitOperations().exceptFlushingData();

        // 3. heuristic for detecting tests in which it's worth to reduce lazy loading
        NPlusOneAssertionRule rule3 = assertions
                .within().lastSession()
                .shouldBe().atMost(2).implicitOperations().exceptFlushingData();

        // 4. ensuring no more implicit operations in specific test case appeared
        NPlusOneAssertionRule rule4 = assertions
                .within().lastSession()
                .shouldBe().noImplicitOperations().exceptAnyOf(builder -> builder
                        .flushingData() // times() & statements()
                        .loadingEntity(Class.class).times(5)
                        .loadingEntity(Class.class)
                        .loadingAnyCollection()
                );

        // 5. SQL statistics (implicit & explicit)
        NPlusOneAssertionRule rule5 = assertions
                .within().lastSession()
                .shouldBe().atMost(10).sqlStatementsTotal(SELECT_STATEMENT)
                .andShouldBe().atMost(2).sqlStatementsTotal(INSERT_STATEMENT)
                .andShouldBe().none().sqlStatementsTotal(OTHER_STATEMENTS);

        // 6. ensuring there is expected amount of explicit operations
        NPlusOneAssertionRule rule6 = assertions
                .within().lastSession()
                .shouldBe().atMost(2).explicitOperations();

        // 7. ensuring that explicit operations are used only for fetching
        NPlusOneAssertionRule rule7 = assertions
                .within().lastSession()
                .shouldBe().noExplicitOperations().exceptFetchingData();

        // 8. ensuring that explicit operations are used only for modifications
        NPlusOneAssertionRule rule8 = assertions
                .within().lastSession()
                .shouldBe().noExplicitOperations().exceptModifyingData();

        // 9. ensuring that explicit operations are used only for fetching
        NPlusOneAssertionRule rule9 = assertions
                .within().lastSession()
                .shouldBe().noExplicitOperations().exceptFetchingDataVia(Class.class, "method");


        //------------------------------------------------------------------

        NPlusOneAssertionRule rule10 = assertions
                .within().lastSession().insideExecutionOfAnyMethodIn(Class.class)
                .shouldBe().noImplicitOperations().exceptAnyOf(exclusions -> exclusions
                        .loadingCollection(Class.class, "collection")
                        .loadingEntity(Class.class)
                        .flushingData()
                );

        NPlusOneAssertionRule rule11 = assertions
                .within().lastSession().insideExecutionOfAnyMethodIn(Class.class)
                .shouldBe().noImplicitOperations().exceptAllOf(exclusions -> exclusions
                        .loadingCollection(Class.class, "collection").times(1)
                        .loadingEntity(Class.class).times(exactly(1)).usingSqlStatements().times(atMost(5))
                        .flushingData()
                );

        NPlusOneAssertionRule rule12 = assertions
                .within().lastSession().insideExecutionOfAnyMethodIn(Class.class)
                .shouldBe().noExplicitOperations().exceptAllOfInOrder(exclusions -> exclusions
                        .fetchingDataVia(Class.class, "someMethod").times(1)
                        .modifyingData().times(1).usingSqlStatements(UPDATE_STATEMENT)
                        .modifyingData().times(1).usingSqlStatements(INSERT_STATEMENT).times(5)
                );

    }
}