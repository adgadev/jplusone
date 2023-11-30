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

package com.adgadev.jplusone.test.domain.commerce;

import com.adgadev.jplusone.core.registry.OperationNodeView;
import com.adgadev.jplusone.core.registry.OperationType;
import com.adgadev.jplusone.core.registry.RootNodeView;
import com.adgadev.jplusone.core.registry.SessionNodeView;
import com.adgadev.jplusone.core.registry.StatementNodeView;
import com.adgadev.jplusone.core.registry.StatementType;
import com.adgadev.jplusone.test.matchers.JPlusOneMatchers;
import com.adgadev.jplusone.test.matchers.frame.FrameExtractSpecification;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;

import jakarta.persistence.EntityManager;
import java.util.List;

import static com.adgadev.jplusone.core.registry.LazyInitialisation.collectionLazyInitialisation;
import static com.adgadev.jplusone.core.registry.LazyInitialisation.entityLazyInitialisation;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


@ActiveProfiles("integration-test")
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class CommerceServiceTest {

    @Autowired
    private CommerceService commerceService;

    @Autowired
    private RootNodeView rootNode;

    @Test
    void shouldLoadVariousObjects() {
        // given
        int sessionAmount = rootNode.getSessions().size();

        // when
        commerceService.loadVariousObjects();

        // then
        assertThat(rootNode.getSessions(), hasSize(equalTo(sessionAmount + 1)));

        SessionNodeView sessionNode = rootNode.getSessions().get(rootNode.getSessions().size() - 1);
        assertThat(sessionNode, notNullValue());
        assertThat(sessionNode.getOperations(), hasSize(equalTo(7)));

        assertThat(sessionNode.getSessionFrameStack(), JPlusOneMatchers.frameCallSequenceMatcher(List.of(
                FrameExtractSpecification.anyAppMethodCallFrame(CommerceServiceTest.class, "shouldLoadVariousObjects"),
                FrameExtractSpecification.anyProxyMethodCallFrame(CommerceService.class, "loadVariousObjects")
        )));

        OperationNodeView operationNodeView1 = sessionNode.getOperations().get(0);
        assertThat(operationNodeView1, notNullValue());
        assertThat(operationNodeView1.getOperationType(), equalTo(OperationType.EXPLICIT));
        assertThat(operationNodeView1.getStatements(), hasSize(equalTo(1)));
        assertThat(operationNodeView1.getLazyInitialisations(), empty());
        assertThat(operationNodeView1.getCallFramesStack(), JPlusOneMatchers.frameCallSequenceMatcher(List.of(
                FrameExtractSpecification.anyAppMethodCallFrame(CommerceService.class, "loadVariousObjects"),
                FrameExtractSpecification.anyThirdPartyMethodCallFrameOnClassAssignableFrom(EntityManager.class, "find")
        )));

        OperationNodeView operationNodeView2 = sessionNode.getOperations().get(1);
        assertThat(operationNodeView2, notNullValue());
        assertThat(operationNodeView2.getOperationType(), equalTo(OperationType.EXPLICIT));
        assertThat(operationNodeView2.getStatements(), hasSize(equalTo(1)));
        assertThat(operationNodeView2.getLazyInitialisations(), empty());
        assertThat(operationNodeView2.getCallFramesStack(), JPlusOneMatchers.frameCallSequenceMatcher(List.of(
                FrameExtractSpecification.anyAppMethodCallFrame(CommerceService.class, "loadVariousObjects"),
                FrameExtractSpecification.anyThirdPartyMethodCallFrameOnClassAssignableFrom(EntityManager.class, "find")
        )));

        OperationNodeView operationNodeView3 = sessionNode.getOperations().get(2);
        assertThat(operationNodeView3, notNullValue());
        assertThat(operationNodeView3.getOperationType(), equalTo(OperationType.IMPLICIT));
        assertThat(operationNodeView3.getStatements(), hasSize(equalTo(1)));
        assertThat(operationNodeView3.getLazyInitialisations(), contains(entityLazyInitialisation(ClientProfile.class.getName())));
        assertThat(operationNodeView3.getCallFramesStack(), JPlusOneMatchers.frameCallSequenceMatcher(List.of(
                FrameExtractSpecification.anyAppMethodCallFrame(CommerceService.class, "loadVariousObjects"),
                FrameExtractSpecification.anyProxyMethodCallFrame(ClientProfile.class, "getPhotoLink")
        )));

        OperationNodeView operationNodeView4 = sessionNode.getOperations().get(3);
        assertThat(operationNodeView4, notNullValue());
        assertThat(operationNodeView4.getOperationType(), equalTo(OperationType.IMPLICIT));
        assertThat(operationNodeView4.getStatements(), hasSize(equalTo(1)));
        assertThat(operationNodeView4.getLazyInitialisations(), contains(entityLazyInitialisation(ClientProfile.class.getName())));
        assertThat(operationNodeView4.getCallFramesStack(), JPlusOneMatchers.frameCallSequenceMatcher(List.of(
                FrameExtractSpecification.anyAppMethodCallFrame(CommerceService.class, "loadVariousObjects"),
                FrameExtractSpecification.anyProxyMethodCallFrame(ClientProfile.class, "getPhotoLink")  // TODO: verify if stack is correct
        )));

        OperationNodeView operationNodeView5 = sessionNode.getOperations().get(4);
        assertThat(operationNodeView5, notNullValue());
        assertThat(operationNodeView5.getOperationType(), equalTo(OperationType.IMPLICIT));
        assertThat(operationNodeView5.getStatements(), hasSize(equalTo(1)));
        assertThat(operationNodeView5.getLazyInitialisations(), contains(collectionLazyInitialisation(Client.class.getName(), "orders")));
        assertThat(operationNodeView5.getCallFramesStack(), JPlusOneMatchers.frameCallSequenceMatcher(List.of(
                FrameExtractSpecification.anyAppMethodCallFrame(CommerceService.class, "loadVariousObjects")
        )));

        OperationNodeView operationNodeView6 = sessionNode.getOperations().get(5);
        assertThat(operationNodeView6, notNullValue());
        assertThat(operationNodeView6.getOperationType(), equalTo(OperationType.IMPLICIT));
        assertThat(operationNodeView6.getStatements(), hasSize(equalTo(1)));
        assertThat(operationNodeView6.getLazyInitialisations(), contains(collectionLazyInitialisation(Order.class.getName(), "products")));
        assertThat(operationNodeView6.getCallFramesStack(), JPlusOneMatchers.frameCallSequenceMatcher(List.of(
                FrameExtractSpecification.anyAppMethodCallFrame(CommerceService.class, "loadVariousObjects")
        )));

        OperationNodeView operationNodeView7 = sessionNode.getOperations().get(6);
        assertThat(operationNodeView7, notNullValue());
        assertThat(operationNodeView7.getOperationType(), equalTo(OperationType.IMPLICIT));
        assertThat(operationNodeView7.getStatements(), hasSize(equalTo(1)));
        assertThat(operationNodeView7.getLazyInitialisations(), contains(entityLazyInitialisation(Manufacturer.class.getName())));
        assertThat(operationNodeView7.getCallFramesStack(), JPlusOneMatchers.frameCallSequenceMatcher(List.of(
                FrameExtractSpecification.anyAppMethodCallFrame(CommerceService.class, "loadVariousObjects"),
                FrameExtractSpecification.anyProxyMethodCallFrame(Manufacturer.class, "getName")
        )));


        StatementNodeView statementNodeView1 = operationNodeView1.getStatements().get(0);
        assertThat(statementNodeView1, notNullValue());
        assertThat(statementNodeView1.getStatementType(), equalTo(StatementType.SELECT));
        assertThat(statementNodeView1.getSql(), endsWith("from client c1_0 where c1_0.id=1"));

        StatementNodeView statementNodeView2 = operationNodeView2.getStatements().get(0);
        assertThat(statementNodeView2, notNullValue());
        assertThat(statementNodeView2.getStatementType(), equalTo(StatementType.SELECT));
        assertThat(statementNodeView2.getSql(), endsWith("from users u1_0 where u1_0.client_id=1"));

        StatementNodeView statementNodeView3 = operationNodeView3.getStatements().get(0);
        assertThat(statementNodeView3, notNullValue());
        assertThat(statementNodeView3.getStatementType(), equalTo(StatementType.SELECT));
        assertThat(statementNodeView3.getSql(), endsWith("from client_profile c1_0 where c1_0.id=1"));

        StatementNodeView statementNodeView4 = operationNodeView4.getStatements().get(0);
        assertThat(statementNodeView4, notNullValue());
        assertThat(statementNodeView4.getStatementType(), equalTo(StatementType.SELECT));
        assertThat(statementNodeView4.getSql(), endsWith("from client c1_0 where c1_0.client_profile_id=1"));

        StatementNodeView statementNodeView5 = operationNodeView5.getStatements().get(0);
        assertThat(statementNodeView5, notNullValue());
        assertThat(statementNodeView5.getStatementType(), equalTo(StatementType.SELECT));
        assertThat(statementNodeView5.getSql(), endsWith("from orders o1_0 where o1_0.client_id=1"));

        StatementNodeView statementNodeView6 = operationNodeView6.getStatements().get(0);
        assertThat(statementNodeView6, notNullValue());
        assertThat(statementNodeView6.getStatementType(), equalTo(StatementType.SELECT));
        assertThat(statementNodeView6.getSql(), endsWith("from order_product p1_0 join product p1_1 on p1_1.id=p1_0.product_id where p1_0.order_id=1"));

        StatementNodeView statementNodeView7 = operationNodeView7.getStatements().get(0);
        assertThat(statementNodeView7, notNullValue());
        assertThat(statementNodeView7.getStatementType(), equalTo(StatementType.SELECT));
        assertThat(statementNodeView7.getSql(), endsWith("from manufacturer m1_0 where m1_0.id=1"));
    }

}