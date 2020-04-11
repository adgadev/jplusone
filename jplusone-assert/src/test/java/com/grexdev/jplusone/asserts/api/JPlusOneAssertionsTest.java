package com.grexdev.jplusone.asserts.api;

import static com.grexdev.jplusone.asserts.api.builder.AmountMatcher.atMost;
import static com.grexdev.jplusone.asserts.api.builder.AmountMatcher.exactly;
import static com.grexdev.jplusone.asserts.api.builder.SqlStatementGroupType.OTHER_STATEMENTS;
import static com.grexdev.jplusone.asserts.api.builder.SqlStatementType.*;

class JPlusOneAssertionsTest {

    void test(JPlusOneAssertions assertions) {
        // TODO: API use cases

        // 1. strict checking on all
        JPlusOneAssertionRule rule1 = assertions
                .within().lastSession()
                .shouldBe().noImplicitOperations();

        // 2. strict checking without flush
        JPlusOneAssertionRule rule2 = assertions
                .within().lastSession()
                .shouldBe().noImplicitOperations().exceptFlushingData();

        // 3. heuristic for detecting tests in which it's worth to reduce lazy loading
        JPlusOneAssertionRule rule3 = assertions
                .within().lastSession()
                .shouldBe().atMost(2).implicitOperations().exceptFlushingData();

        // 4. ensuring no more implicit operations in specific test case appeared
        JPlusOneAssertionRule rule4 = assertions
                .within().lastSession()
                .shouldBe().noImplicitOperations().exceptAnyOf(builder -> builder
                        .flushingData() // times() & statements()
                        .loadingEntity(Class.class).times(5)
                        .loadingEntity(Class.class)
                        .loadingAnyCollection()
                );

        // 5. SQL statistics (implicit & explicit)
        JPlusOneAssertionRule rule5 = assertions
                .within().lastSession()
                .shouldBe().atMost(10).sqlStatementsTotal(SELECT_STATEMENT)
                .andShouldBe().atMost(2).sqlStatementsTotal(INSERT_STATEMENT)
                .andShouldBe().none().sqlStatementsTotal(OTHER_STATEMENTS);

        // 6. ensuring there is expected amount of explicit operations
        JPlusOneAssertionRule rule6 = assertions
                .within().lastSession()
                .shouldBe().atMost(2).explicitOperations();

        // 7. ensuring that explicit operations are used only for fetching
        JPlusOneAssertionRule rule7 = assertions
                .within().lastSession()
                .shouldBe().noExplicitOperations().exceptFetchingData();

        // 8. ensuring that explicit operations are used only for modifications
        JPlusOneAssertionRule rule8 = assertions
                .within().lastSession()
                .shouldBe().noExplicitOperations().exceptModifyingData();

        // 9. ensuring that explicit operations are used only for fetching
        JPlusOneAssertionRule rule9 = assertions
                .within().lastSession()
                .shouldBe().noExplicitOperations().exceptFetchingDataVia(Class.class, "method");


        //------------------------------------------------------------------

        JPlusOneAssertionRule rule10 = assertions
                .within().lastSession().insideExecutionOfAnyMethodIn(Class.class)
                .shouldBe().noImplicitOperations().exceptAnyOf(exclusions -> exclusions
                        .loadingCollection(Class.class, "collection")
                        .loadingEntity(Class.class)
                        .flushingData()
                );

        JPlusOneAssertionRule rule11 = assertions
                .within().lastSession().insideExecutionOfAnyMethodIn(Class.class)
                .shouldBe().noImplicitOperations().exceptAllOf(exclusions -> exclusions
                        .loadingCollection(Class.class, "collection").times(1)
                        .loadingEntity(Class.class).times(exactly(1)).usingSqlStatements().times(atMost(5))
                        .flushingData()
                );

        JPlusOneAssertionRule rule12 = assertions
                .within().lastSession().insideExecutionOfAnyMethodIn(Class.class)
                .shouldBe().noExplicitOperations().exceptAllOfInOrder(exclusions -> exclusions
                        .fetchingDataVia(Class.class, "someMethod").times(1)
                        .modifyingData().times(1).usingSqlStatements(UPDATE_STATEMENT)
                        .modifyingData().times(1).usingSqlStatements(INSERT_STATEMENT).times(5)
                );

    }
}