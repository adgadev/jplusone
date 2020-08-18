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

package com.grexdev.jplusone.test.domain.commerce.session;

import com.grexdev.jplusone.core.registry.OperationNodeView;
import com.grexdev.jplusone.core.registry.RootNodeView;
import com.grexdev.jplusone.core.registry.SessionNodeView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.grexdev.jplusone.test.matchers.JPlusOneMatchers.allFrameCallMatcher;
import static com.grexdev.jplusone.test.matchers.JPlusOneMatchers.frameCallSequenceMatcher;
import static com.grexdev.jplusone.test.matchers.frame.FrameExtractSpecification.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@ActiveProfiles("integration-test")
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@TestMethodOrder(OrderAnnotation.class)
class OuterSessionServiceTest {

    private static final Long MANUFACTURER_ID = 1L;

    @Autowired
    private OuterSessionService service;

    @Autowired
    private RootNodeView rootNode;

    private RootNodeAssertionWrapper rootNodeWrapper;

    @BeforeEach
    public void setup() {
        rootNodeWrapper = new RootNodeAssertionWrapper(rootNode);
    }

    @Test
    void shouldCheckIfEntityManagerOpenInTx() {
        // when
        service.checkIfEntityManagerOpenInTx();

        // then
        assertThat(rootNodeWrapper.getNewSessionsAmount(), equalTo(1));
        SessionNodeView sessionNode = rootNodeWrapper.getFirstNewSession();
        assertThat(sessionNode.getOperations(), hasSize(equalTo(0)));
    }

    @Test
    void shouldCheckIfEntityManagerOpenNoTx() {
        // when
        service.checkIfEntityManagerOpenNoTx();

        // then
        assertThat(rootNodeWrapper.getNewSessionsAmount(), equalTo(0));
    }

    @Test
    void shouldCreateQueryInTx() {
        // when
        service.createQueryInTx();

        // then
        assertThat(rootNodeWrapper.getNewSessionsAmount(), equalTo(1));
        SessionNodeView sessionNode = rootNodeWrapper.getFirstNewSession();
        assertThat(sessionNode.getOperations(), hasSize(equalTo(0)));
    }

    @Test
    void shouldCreateQueryNoTx() {
        // when
        service.createQueryNoTx();

        // then
        assertThat(rootNodeWrapper.getNewSessionsAmount(), equalTo(0));
    }

    @Test
    void shouldFetchDataOuterTx() {
        // when
        service.fetchDataOuterTx();

        // then
        assertThat(rootNodeWrapper.getNewSessionsAmount(), equalTo(1));

        SessionNodeView sessionNode = rootNodeWrapper.getFirstNewSession();
        assertThat(sessionNode.getOperations(), hasSize(equalTo(1)));
        assertThat(sessionNode.getSessionFrameStack(), frameCallSequenceMatcher(List.of(
                anyAppMethodCallFrame(OuterSessionServiceTest.class, "shouldFetchDataOuterTx"),
                anyProxyMethodCallFrame(OuterSessionService.class, "fetchDataOuterTx")
        )));

        OperationNodeView operationNodeView1 = sessionNode.getOperations().get(0);
        assertThat(operationNodeView1.getStatements(), hasSize(equalTo(1)));
        assertThat(operationNodeView1.getCallFramesStack(), frameCallSequenceMatcher(List.of(
                anyAppMethodCallFrame(OuterSessionService.class, "fetchDataOuterTx"),
                anyAppMethodCallFrame(OuterSessionService.class, "fetchData")
        )));
    }

    @Test
    void shouldFetchDataInnerNewTx() {
        // when
        service.fetchDataInnerNewTx();

        // then
        assertThat(rootNodeWrapper.getNewSessionsAmount(), equalTo(2));

        SessionNodeView sessionNode1 = rootNodeWrapper.getNewSession(0);
        SessionNodeView sessionNode2 = rootNodeWrapper.getNewSession(1);
        assertThat(sessionNode1.getOperations(), hasSize(equalTo(1)));
        assertThat(sessionNode2.getOperations(), hasSize(equalTo(0)));

        assertThat(sessionNode1.getSessionFrameStack(), frameCallSequenceMatcher(List.of(
                anyAppMethodCallFrame(OuterSessionServiceTest.class, "shouldFetchDataInnerNewTx"),
                anyProxyMethodCallFrame(OuterSessionService.class, "fetchDataInnerNewTx"),
                anyAppMethodCallFrame(OuterSessionService.class, "fetchDataInnerNewTx"),
                anyProxyMethodCallFrame(InnerSessionService.class, "fetchDataInNewTransaction")
        )));

        assertThat(sessionNode2.getSessionFrameStack(), nullValue()); // TODO: sessions without operations / frame stack should not be added - investigate

        OperationNodeView operationNodeView1 = sessionNode1.getOperations().get(0);
        assertThat(operationNodeView1.getStatements(), hasSize(equalTo(1)));
        assertThat(operationNodeView1.getCallFramesStack(), frameCallSequenceMatcher(List.of(
                anyAppMethodCallFrame(InnerSessionService.class, "fetchDataInNewTransaction")
        )));
    }

    @Test
    void shouldFetchDataOuterTxInnerTx() {
        // when
        service.fetchDataOuterTxInnerTx();

        // then
        assertThat(rootNodeWrapper.getNewSessionsAmount(), equalTo(1));

        SessionNodeView sessionNode = rootNodeWrapper.getFirstNewSession();
        assertThat(sessionNode.getOperations(), hasSize(equalTo(2)));
        assertThat(sessionNode.getSessionFrameStack(), frameCallSequenceMatcher(List.of(
                anyAppMethodCallFrame(OuterSessionServiceTest.class, "shouldFetchDataOuterTxInnerTx"),
                anyProxyMethodCallFrame(OuterSessionService.class, "fetchDataOuterTxInnerTx")
        )));

        OperationNodeView operationNodeView1 = sessionNode.getOperations().get(0);
        assertThat(operationNodeView1.getStatements(), hasSize(equalTo(1)));
        assertThat(operationNodeView1.getCallFramesStack(), frameCallSequenceMatcher(List.of(
                anyAppMethodCallFrame(OuterSessionService.class, "fetchDataOuterTxInnerTx"),
                anyAppMethodCallFrame(OuterSessionService.class, "fetchData")
        )));

        OperationNodeView operationNodeView2 = sessionNode.getOperations().get(1);
        assertThat(operationNodeView2.getStatements(), hasSize(equalTo(1)));
        assertThat(operationNodeView2.getCallFramesStack(), frameCallSequenceMatcher(List.of(
                anyAppMethodCallFrame(OuterSessionService.class, "fetchDataOuterTxInnerTx"),
                anyProxyMethodCallFrame(InnerSessionService.class, "fetchDataInExistingTransaction"),
                anyAppMethodCallFrame(InnerSessionService.class, "fetchDataInExistingTransaction")
        )));
    }

    @Test
    void shouldFetchDataOuterTxInnerNewTx() {
        // when
        service.fetchDataOuterTxInnerNewTx();

        // then
        assertThat(rootNodeWrapper.getNewSessionsAmount(), equalTo(2));

        SessionNodeView sessionNode1 = rootNodeWrapper.getNewSession(0);
        SessionNodeView sessionNode2 = rootNodeWrapper.getNewSession(1); // session ordered by session close date
        assertThat(sessionNode1.getOperations(), hasSize(equalTo(1)));
        assertThat(sessionNode2.getOperations(), hasSize(equalTo(1)));

        assertThat(sessionNode1.getSessionFrameStack(), frameCallSequenceMatcher(List.of(
                anyAppMethodCallFrame(OuterSessionServiceTest.class, "shouldFetchDataOuterTxInnerNewTx"),
                anyProxyMethodCallFrame(OuterSessionService.class, "fetchDataOuterTxInnerNewTx"),
                anyAppMethodCallFrame(OuterSessionService.class, "fetchDataOuterTxInnerNewTx"),
                anyProxyMethodCallFrame(InnerSessionService.class, "fetchDataInNewTransaction")
        )));

        assertThat(sessionNode2.getSessionFrameStack(), frameCallSequenceMatcher(List.of(
                anyAppMethodCallFrame(OuterSessionServiceTest.class, "shouldFetchDataOuterTxInnerNewTx"),
                anyProxyMethodCallFrame(OuterSessionService.class, "fetchDataOuterTxInnerNewTx")
        )));

        OperationNodeView operationNodeView1 = sessionNode1.getOperations().get(0);
        assertThat(operationNodeView1.getStatements(), hasSize(equalTo(1)));
        assertThat(operationNodeView1.getCallFramesStack(), frameCallSequenceMatcher(List.of(
                anyAppMethodCallFrame(InnerSessionService.class, "fetchDataInNewTransaction")
        )));

        OperationNodeView operationNodeView2 = sessionNode2.getOperations().get(0);
        assertThat(operationNodeView2.getStatements(), hasSize(equalTo(1)));
        assertThat(operationNodeView2.getCallFramesStack(), frameCallSequenceMatcher(List.of(
                anyAppMethodCallFrame(OuterSessionService.class, "fetchDataOuterTxInnerNewTx"),
                anyAppMethodCallFrame(OuterSessionService.class, "fetchData")
        )));
    }

    @Test
    void shouldFetchDataOuterNoTxInnerTx() {
        // when
        service.fetchDataOuterNoTxInnerTx();

        // then
        assertThat(rootNodeWrapper.getNewSessionsAmount(), equalTo(2));

        SessionNodeView sessionNode1 = rootNodeWrapper.getNewSession(0);
        SessionNodeView sessionNode2 = rootNodeWrapper.getNewSession(1);
        assertThat(sessionNode1.getOperations(), hasSize(equalTo(1)));
        assertThat(sessionNode2.getOperations(), hasSize(equalTo(1)));

        assertThat(sessionNode1.getSessionFrameStack(), frameCallSequenceMatcher(List.of(
                anyAppMethodCallFrame(OuterSessionServiceTest.class, "shouldFetchDataOuterNoTxInnerTx"),
                anyProxyMethodCallFrame(OuterSessionService.class, "fetchDataOuterNoTxInnerTx"),
                anyAppMethodCallFrame(OuterSessionService.class, "fetchDataOuterNoTxInnerTx"),
                anyAppMethodCallFrame(OuterSessionService.class, "fetchData")
        )));

        assertThat(sessionNode2.getSessionFrameStack(), frameCallSequenceMatcher(List.of(
                anyAppMethodCallFrame(OuterSessionServiceTest.class, "shouldFetchDataOuterNoTxInnerTx"),
                anyProxyMethodCallFrame(OuterSessionService.class, "fetchDataOuterNoTxInnerTx"),
                anyAppMethodCallFrame(OuterSessionService.class, "fetchDataOuterNoTxInnerTx"),
                anyProxyMethodCallFrame(InnerSessionService.class, "fetchDataInExistingTransaction")
        )));

        OperationNodeView operationNodeView1 = sessionNode1.getOperations().get(0);
        assertThat(operationNodeView1.getStatements(), hasSize(equalTo(1)));
        assertThat(operationNodeView1.getCallFramesStack(), allFrameCallMatcher(notAppMethodCallFrame()));

        OperationNodeView operationNodeView2 = sessionNode2.getOperations().get(0);
        assertThat(operationNodeView2.getStatements(), hasSize(equalTo(1)));
        assertThat(operationNodeView2.getCallFramesStack(), frameCallSequenceMatcher(List.of(
                anyAppMethodCallFrame(InnerSessionService.class, "fetchDataInExistingTransaction")
        )));
    }

    @Test
    void shouldFetchDataOuterNoTxInnerNewTx() {
        // when
        service.fetchDataOuterNoTxInnerNewTx();

        // then
        assertThat(rootNodeWrapper.getNewSessionsAmount(), equalTo(2));

        SessionNodeView sessionNode1 = rootNodeWrapper.getNewSession(0);
        SessionNodeView sessionNode2 = rootNodeWrapper.getNewSession(1);
        assertThat(sessionNode1.getOperations(), hasSize(equalTo(1)));
        assertThat(sessionNode2.getOperations(), hasSize(equalTo(1)));

        assertThat(sessionNode1.getSessionFrameStack(), frameCallSequenceMatcher(List.of(
                anyAppMethodCallFrame(OuterSessionServiceTest.class, "shouldFetchDataOuterNoTxInnerNewTx"),
                anyProxyMethodCallFrame(OuterSessionService.class, "fetchDataOuterNoTxInnerNewTx"),
                anyAppMethodCallFrame(OuterSessionService.class, "fetchDataOuterNoTxInnerNewTx"),
                anyAppMethodCallFrame(OuterSessionService.class, "fetchData")
        )));

        assertThat(sessionNode2.getSessionFrameStack(), frameCallSequenceMatcher(List.of(
                anyAppMethodCallFrame(OuterSessionServiceTest.class, "shouldFetchDataOuterNoTxInnerNewTx"),
                anyProxyMethodCallFrame(OuterSessionService.class, "fetchDataOuterNoTxInnerNewTx"),
                anyAppMethodCallFrame(OuterSessionService.class, "fetchDataOuterNoTxInnerNewTx"),
                anyProxyMethodCallFrame(InnerSessionService.class, "fetchDataInNewTransaction")
        )));

        OperationNodeView operationNodeView1 = sessionNode1.getOperations().get(0);
        assertThat(operationNodeView1.getStatements(), hasSize(equalTo(1)));
        assertThat(operationNodeView1.getCallFramesStack(), allFrameCallMatcher(notAppMethodCallFrame()));

        OperationNodeView operationNodeView2 = sessionNode2.getOperations().get(0);
        assertThat(operationNodeView2.getStatements(), hasSize(equalTo(1)));
        assertThat(operationNodeView2.getCallFramesStack(), frameCallSequenceMatcher(List.of(
                anyAppMethodCallFrame(InnerSessionService.class, "fetchDataInNewTransaction")
        )));
    }

    @Test
    void shouldFetchDataInnerTxOuterTx() {
        // when
        service.fetchDataInnerTxOuterTx();

        // then
        assertThat(rootNodeWrapper.getNewSessionsAmount(), equalTo(1));

        SessionNodeView sessionNode = rootNodeWrapper.getFirstNewSession();
        assertThat(sessionNode.getOperations(), hasSize(equalTo(2)));
        assertThat(sessionNode.getSessionFrameStack(), frameCallSequenceMatcher(List.of(
                anyAppMethodCallFrame(OuterSessionServiceTest.class, "shouldFetchDataInnerTxOuterTx"),
                anyProxyMethodCallFrame(OuterSessionService.class, "fetchDataInnerTxOuterTx")
        )));

        OperationNodeView operationNodeView2 = sessionNode.getOperations().get(0);
        assertThat(operationNodeView2.getStatements(), hasSize(equalTo(1)));
        assertThat(operationNodeView2.getCallFramesStack(), frameCallSequenceMatcher(List.of(
                anyAppMethodCallFrame(OuterSessionService.class, "fetchDataInnerTxOuterTx"),
                anyProxyMethodCallFrame(InnerSessionService.class, "fetchDataInExistingTransaction"),
                anyAppMethodCallFrame(InnerSessionService.class, "fetchDataInExistingTransaction")
        )));

        OperationNodeView operationNodeView1 = sessionNode.getOperations().get(1);
        assertThat(operationNodeView1.getStatements(), hasSize(equalTo(1)));
        assertThat(operationNodeView1.getCallFramesStack(), frameCallSequenceMatcher(List.of(
                anyAppMethodCallFrame(OuterSessionService.class, "fetchDataInnerTxOuterTx"),
                anyAppMethodCallFrame(OuterSessionService.class, "fetchData")
        )));
    }

    @Test
    void shouldFetchDataInnerNewTxOuterTx() {
        // when
        service.fetchDataInnerNewTxOuterTx();

        // then
        assertThat(rootNodeWrapper.getNewSessionsAmount(), equalTo(2));

        SessionNodeView sessionNode1 = rootNodeWrapper.getNewSession(0);
        SessionNodeView sessionNode2 = rootNodeWrapper.getNewSession(1); // session ordered by session close date
        assertThat(sessionNode1.getOperations(), hasSize(equalTo(1)));
        assertThat(sessionNode2.getOperations(), hasSize(equalTo(1)));

        assertThat(sessionNode1.getSessionFrameStack(), frameCallSequenceMatcher(List.of(
                anyAppMethodCallFrame(OuterSessionServiceTest.class, "shouldFetchDataInnerNewTxOuterTx"),
                anyProxyMethodCallFrame(OuterSessionService.class, "fetchDataInnerNewTxOuterTx"),
                anyAppMethodCallFrame(OuterSessionService.class, "fetchDataInnerNewTxOuterTx"),
                anyProxyMethodCallFrame(InnerSessionService.class, "fetchDataInNewTransaction")
        )));

        assertThat(sessionNode2.getSessionFrameStack(), frameCallSequenceMatcher(List.of(
                anyAppMethodCallFrame(OuterSessionServiceTest.class, "shouldFetchDataInnerNewTxOuterTx"),
                anyProxyMethodCallFrame(OuterSessionService.class, "fetchDataInnerNewTxOuterTx")
        )));

        OperationNodeView operationNodeView1 = sessionNode1.getOperations().get(0);
        assertThat(operationNodeView1.getStatements(), hasSize(equalTo(1)));
        assertThat(operationNodeView1.getCallFramesStack(), frameCallSequenceMatcher(List.of(
                anyAppMethodCallFrame(InnerSessionService.class, "fetchDataInNewTransaction")
        )));

        OperationNodeView operationNodeView2 = sessionNode2.getOperations().get(0);
        assertThat(operationNodeView2.getStatements(), hasSize(equalTo(1)));
        assertThat(operationNodeView2.getCallFramesStack(), frameCallSequenceMatcher(List.of(
                anyAppMethodCallFrame(OuterSessionService.class, "fetchDataInnerNewTxOuterTx"),
                anyAppMethodCallFrame(OuterSessionService.class, "fetchData")
        )));
    }

    @Test
    void shouldFetchDataOuterTxInnerNewTxOuterTx() {
        // when
        service.fetchDataOuterTxInnerNewTxOuterTx();

        // then
        assertThat(rootNodeWrapper.getNewSessionsAmount(), equalTo(2));

        SessionNodeView sessionNode1 = rootNodeWrapper.getNewSession(0);
        SessionNodeView sessionNode2 = rootNodeWrapper.getNewSession(1); // session ordered by session close date
        assertThat(sessionNode1.getOperations(), hasSize(equalTo(1)));
        assertThat(sessionNode2.getOperations(), hasSize(equalTo(2)));

        assertThat(sessionNode1.getSessionFrameStack(), frameCallSequenceMatcher(List.of(
                anyAppMethodCallFrame(OuterSessionServiceTest.class, "shouldFetchDataOuterTxInnerNewTxOuterTx"),
                anyProxyMethodCallFrame(OuterSessionService.class, "fetchDataOuterTxInnerNewTxOuterTx"),
                anyAppMethodCallFrame(OuterSessionService.class, "fetchDataOuterTxInnerNewTxOuterTx"),
                anyProxyMethodCallFrame(InnerSessionService.class, "fetchDataInNewTransaction")
        )));

        assertThat(sessionNode2.getSessionFrameStack(), frameCallSequenceMatcher(List.of(
                anyAppMethodCallFrame(OuterSessionServiceTest.class, "shouldFetchDataOuterTxInnerNewTxOuterTx"),
                anyProxyMethodCallFrame(OuterSessionService.class, "fetchDataOuterTxInnerNewTxOuterTx")
        )));

        OperationNodeView operationNodeView1 = sessionNode1.getOperations().get(0);
        assertThat(operationNodeView1.getStatements(), hasSize(equalTo(1)));
        assertThat(operationNodeView1.getCallFramesStack(), frameCallSequenceMatcher(List.of(
                anyAppMethodCallFrame(InnerSessionService.class, "fetchDataInNewTransaction")
        )));

        OperationNodeView operationNodeView2 = sessionNode2.getOperations().get(0);
        assertThat(operationNodeView2.getStatements(), hasSize(equalTo(1)));
        assertThat(operationNodeView2.getCallFramesStack(), frameCallSequenceMatcher(List.of(
                anyAppMethodCallFrame(OuterSessionService.class, "fetchDataOuterTxInnerNewTxOuterTx"),
                anyAppMethodCallFrame(OuterSessionService.class, "fetchData")
        )));
    }

    private static class RootNodeAssertionWrapper {

        private final RootNodeView rootNode;

        private final int initialSessionAmount;

        RootNodeAssertionWrapper(RootNodeView rootNode) {
            this.rootNode = rootNode;
            this.initialSessionAmount = rootNode.getSessions().size();
        }

        int getNewSessionsAmount() {
            return rootNode.getSessions().size() - initialSessionAmount;
        }

        SessionNodeView getFirstNewSession() {
            return getNewSession(0);
        }

        SessionNodeView getNewSession(int sessionOrdinal) {
            return rootNode.getSessions().stream()
                    .skip(initialSessionAmount + sessionOrdinal)
                    .findFirst().get();
        }
    }
}