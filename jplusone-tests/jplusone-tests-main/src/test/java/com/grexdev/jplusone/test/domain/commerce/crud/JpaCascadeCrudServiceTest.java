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

package com.grexdev.jplusone.test.domain.commerce.crud;

import com.grexdev.jplusone.core.registry.LazyInitialisation.LazyInitialisationType;
import com.grexdev.jplusone.core.registry.OperationNodeView;
import com.grexdev.jplusone.core.registry.OperationType;
import com.grexdev.jplusone.core.registry.RootNodeView;
import com.grexdev.jplusone.core.registry.SessionNodeView;
import com.grexdev.jplusone.core.registry.StatementNodeView;
import com.grexdev.jplusone.core.registry.StatementType;
import com.grexdev.jplusone.test.domain.commerce.Manufacturer;
import com.grexdev.jplusone.test.domain.commerce.Product;
import org.hibernate.Session;
import org.hibernate.internal.SessionImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.sql.PreparedStatement;
import java.util.List;

import static com.grexdev.jplusone.test.matchers.JPlusOneMatchers.allFrameCallMatcher;
import static com.grexdev.jplusone.test.matchers.JPlusOneMatchers.frameCallSequenceMatcher;
import static com.grexdev.jplusone.test.matchers.frame.FrameExtractSpecification.*;
import static com.grexdev.jplusone.test.matchers.frame.FrameExtractSpecification.anyThirdPartyMethodCallFrameOnClassAssignableFrom;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;

@ActiveProfiles("integration-test")
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class JpaCascadeCrudServiceTest {

    private static final Long MANUFACTURER_ID = 1L;

    @Autowired
    private JpaCascadeCrudService crudService;

    @Autowired
    private RootNodeView rootNode;

    @Test
    void shouldAddManufacturerProduct() {
        // when
        Product product = crudService.addManufacturerProduct(MANUFACTURER_ID, "Product Nr1");

        // then
        assertThat(rootNode.getSessions(), hasSize(greaterThan(0)));

        SessionNodeView sessionNode = rootNode.getSessions().get(rootNode.getSessions().size() - 1);
        assertThat(sessionNode, notNullValue());
        assertThat(sessionNode.getOperations(), hasSize(equalTo(3)));

        assertThat(sessionNode.getSessionFrameStack(), frameCallSequenceMatcher(List.of(
                anyAppMethodCallFrame(JpaCascadeCrudServiceTest.class, "shouldAddManufacturerProduct"),
                anyProxyMethodCallFrame(JpaCascadeCrudService.class, "addManufacturerProduct")
        )));

        OperationNodeView operationNodeView1 = sessionNode.getOperations().get(0);
        assertThat(operationNodeView1, notNullValue());
        assertThat(operationNodeView1.getOperationType(), equalTo(OperationType.EXPLICIT));
        assertThat(operationNodeView1.getStatements(), hasSize(equalTo(1)));
        assertThat(operationNodeView1.getLazyInitialisation(), nullValue());
        assertThat(operationNodeView1.getCallFramesStack(), frameCallSequenceMatcher(List.of(
                anyAppMethodCallFrame(JpaCascadeCrudService.class, "addManufacturerProduct"),
                anyProxyMethodCallFrame(ManufacturerRepository.class, "findById"),
                anyThirdPartyMethodCallFrameOnClassAssignableFrom(EntityManager.class, "find"),
                anyThirdPartyMethodCallFrameOnClassAssignableFrom(PreparedStatement.class, "executeQuery")
        )));

        OperationNodeView operationNodeView2 = sessionNode.getOperations().get(1);
        assertThat(operationNodeView2, notNullValue());
        assertThat(operationNodeView2.getOperationType(), equalTo(OperationType.IMPLICIT));
        assertThat(operationNodeView2.getStatements(), hasSize(equalTo(1)));
        assertThat(operationNodeView2.getLazyInitialisation(), notNullValue());
        assertThat(operationNodeView2.getLazyInitialisation().getEntityClassName(), equalTo(Manufacturer.class.getName()));
        assertThat(operationNodeView2.getLazyInitialisation().getFieldName(), equalTo("products"));
        assertThat(operationNodeView2.getLazyInitialisation().getType(), equalTo(LazyInitialisationType.COLLECTION));
        assertThat(operationNodeView2.getCallFramesStack(), frameCallSequenceMatcher(List.of(
                anyAppMethodCallFrame(JpaCascadeCrudService.class, "addManufacturerProduct"),
                anyAppMethodCallFrame(Manufacturer.class, "addProduct"),
                anyThirdPartyMethodCallFrameOnClassAssignableFrom(Session.class, "initializeCollection"),
                anyThirdPartyMethodCallFrameOnClassAssignableFrom(PreparedStatement.class, "executeQuery")
        )));

        OperationNodeView operationNodeView3 = sessionNode.getOperations().get(2);
        assertThat(operationNodeView3, notNullValue());
        assertThat(operationNodeView3.getOperationType(), equalTo(OperationType.EXPLICIT)); // TODO: why it's explicit?
        assertThat(operationNodeView3.getStatements(), hasSize(equalTo(1)));
        assertThat(operationNodeView3.getLazyInitialisation(), nullValue());
        assertThat(operationNodeView3.getCallFramesStack(), allFrameCallMatcher(notAppMethodCallFrame()));
        assertThat(operationNodeView3.getCallFramesStack(), frameCallSequenceMatcher(List.of(
                anyThirdPartyMethodCallFrameOnClassAssignableFrom(EntityTransaction.class, "commit"),
                anyThirdPartyMethodCallFrame(SessionImpl.class, "doFlush"),
                anyThirdPartyMethodCallFrameOnClassAssignableFrom(PreparedStatement.class, "executeUpdate")
        )));

        StatementNodeView statementNodeView1 = operationNodeView1.getStatements().get(0);
        assertThat(statementNodeView1, notNullValue());
        assertThat(statementNodeView1.getStatementType(), equalTo(StatementType.SELECT));
        assertThat(statementNodeView1.getSql(), endsWith(String.format(
                "from manufacturer manufactur0_ where manufactur0_.id=%d", MANUFACTURER_ID)));

        StatementNodeView statementNodeView2 = operationNodeView2.getStatements().get(0);
        assertThat(statementNodeView2, notNullValue());
        assertThat(statementNodeView2.getStatementType(), equalTo(StatementType.SELECT));
        assertThat(statementNodeView2.getSql(), endsWith(String.format(
                "from product products0_ where products0_.manufacturer_id=%d", MANUFACTURER_ID)));

        StatementNodeView statementNodeView3 = operationNodeView3.getStatements().get(0);
        assertThat(statementNodeView3, notNullValue());
        assertThat(statementNodeView3.getStatementType(), equalTo(StatementType.INSERT));
        assertThat(statementNodeView3.getSql(), equalTo(String.format(
                "insert into product (manufacturer_id, name, id) values (%d, '%s', %d)", MANUFACTURER_ID, "Product Nr1", product.getId())));
    }

    @Test
    void shouldUpdateManufacturerProductName() {
        // given
        crudService.addManufacturerProduct(MANUFACTURER_ID, "Product Nr2");

        // when
        crudService.updateManufacturerProductName(MANUFACTURER_ID, "Product Nr2", "New Product Nr2");

        // then
        assertThat(rootNode.getSessions(), hasSize(greaterThan(0)));

        SessionNodeView sessionNode = rootNode.getSessions().get(rootNode.getSessions().size() - 1);
        assertThat(sessionNode, notNullValue());
        assertThat(sessionNode.getOperations(), hasSize(equalTo(3)));

        assertThat(sessionNode.getSessionFrameStack(), frameCallSequenceMatcher(List.of(
                anyAppMethodCallFrame(JpaCascadeCrudServiceTest.class, "shouldUpdateManufacturerProductName"),
                anyProxyMethodCallFrame(JpaCascadeCrudService.class, "updateManufacturerProductName")
        )));

        OperationNodeView operationNodeView1 = sessionNode.getOperations().get(0);
        assertThat(operationNodeView1, notNullValue());
        assertThat(operationNodeView1.getOperationType(), equalTo(OperationType.EXPLICIT));
        assertThat(operationNodeView1.getStatements(), hasSize(equalTo(1)));
        assertThat(operationNodeView1.getLazyInitialisation(), nullValue());
        assertThat(operationNodeView1.getCallFramesStack(), frameCallSequenceMatcher(List.of(
                anyAppMethodCallFrame(JpaCascadeCrudService.class, "updateManufacturerProductName"),
                anyAppMethodCallFrame(JpaCascadeCrudService.class, "getManufacturerById"),
                anyProxyMethodCallFrame(ManufacturerRepository.class, "findById"),
                anyThirdPartyMethodCallFrameOnClassAssignableFrom(EntityManager.class, "find"),
                anyThirdPartyMethodCallFrameOnClassAssignableFrom(PreparedStatement.class, "executeQuery")
        )));

        OperationNodeView operationNodeView2 = sessionNode.getOperations().get(1);
        assertThat(operationNodeView2, notNullValue());
        assertThat(operationNodeView2.getOperationType(), equalTo(OperationType.IMPLICIT));
        assertThat(operationNodeView2.getStatements(), hasSize(equalTo(1)));
        assertThat(operationNodeView2.getLazyInitialisation(), notNullValue());
        assertThat(operationNodeView2.getLazyInitialisation().getEntityClassName(), equalTo(Manufacturer.class.getName()));
        assertThat(operationNodeView2.getLazyInitialisation().getFieldName(), equalTo("products"));
        assertThat(operationNodeView2.getLazyInitialisation().getType(), equalTo(LazyInitialisationType.COLLECTION));
        assertThat(operationNodeView2.getCallFramesStack(), frameCallSequenceMatcher(List.of(
                anyAppMethodCallFrame(JpaCascadeCrudService.class, "updateManufacturerProductName"),
                anyAppMethodCallFrame(Manufacturer.class, "addProduct"),
                anyThirdPartyMethodCallFrameOnClassAssignableFrom(Session.class, "initializeCollection"),
                anyThirdPartyMethodCallFrameOnClassAssignableFrom(PreparedStatement.class, "executeQuery")
        )));

        OperationNodeView operationNodeView3 = sessionNode.getOperations().get(2);
        assertThat(operationNodeView3, notNullValue());
        assertThat(operationNodeView3.getOperationType(), equalTo(OperationType.EXPLICIT)); // TODO: why it's explicit?
        assertThat(operationNodeView3.getStatements(), hasSize(equalTo(1)));
        assertThat(operationNodeView3.getLazyInitialisation(), nullValue());
        assertThat(operationNodeView3.getCallFramesStack(), allFrameCallMatcher(notAppMethodCallFrame()));
        assertThat(operationNodeView3.getCallFramesStack(), frameCallSequenceMatcher(List.of(
                anyThirdPartyMethodCallFrameOnClassAssignableFrom(EntityTransaction.class, "commit"),
                anyThirdPartyMethodCallFrame(SessionImpl.class, "doFlush"),
                anyThirdPartyMethodCallFrameOnClassAssignableFrom(PreparedStatement.class, "executeUpdate")
        )));

        StatementNodeView statementNodeView1 = operationNodeView1.getStatements().get(0);
        assertThat(statementNodeView1, notNullValue());
        assertThat(statementNodeView1.getStatementType(), equalTo(StatementType.SELECT));
        assertThat(statementNodeView1.getSql(), endsWith(String.format(
                "from manufacturer manufactur0_ where manufactur0_.id=%d", MANUFACTURER_ID)));

        StatementNodeView statementNodeView2 = operationNodeView2.getStatements().get(0);
        assertThat(statementNodeView2, notNullValue());
        assertThat(statementNodeView2.getStatementType(), equalTo(StatementType.SELECT));
        assertThat(statementNodeView2.getSql(), endsWith(String.format(
                "from product products0_ where products0_.manufacturer_id=%d", MANUFACTURER_ID)));

        StatementNodeView statementNodeView3 = operationNodeView3.getStatements().get(0);
        assertThat(statementNodeView3, notNullValue());
        assertThat(statementNodeView3.getStatementType(), equalTo(StatementType.INSERT));
        assertThat(statementNodeView3.getSql(), startsWith(String.format(
                "insert into product (manufacturer_id, name, id) values (%d, '%s'", MANUFACTURER_ID, "New Product Nr2")));
    }

    @Test
    void shouldDeleteManufacturerProduct() {
        // given
        Product product = crudService.addManufacturerProduct(MANUFACTURER_ID, "Product Nr3");

        // when
        crudService.deleteManufacturerProduct(MANUFACTURER_ID, "Product Nr3");

        // then
        assertThat(rootNode.getSessions(), hasSize(greaterThan(0)));

        SessionNodeView sessionNode = rootNode.getSessions().get(rootNode.getSessions().size() - 1);
        assertThat(sessionNode, notNullValue());
        assertThat(sessionNode.getOperations(), hasSize(equalTo(3)));

        assertThat(sessionNode.getSessionFrameStack(), frameCallSequenceMatcher(List.of(
                anyAppMethodCallFrame(JpaCascadeCrudServiceTest.class, "shouldDeleteManufacturerProduct"),
                anyProxyMethodCallFrame(JpaCascadeCrudService.class, "deleteManufacturerProduct")
        )));

        OperationNodeView operationNodeView1 = sessionNode.getOperations().get(0);
        assertThat(operationNodeView1, notNullValue());
        assertThat(operationNodeView1.getOperationType(), equalTo(OperationType.EXPLICIT));
        assertThat(operationNodeView1.getStatements(), hasSize(equalTo(1)));
        assertThat(operationNodeView1.getLazyInitialisation(), nullValue());
        assertThat(operationNodeView1.getCallFramesStack(), frameCallSequenceMatcher(List.of(
                anyAppMethodCallFrame(JpaCascadeCrudService.class, "deleteManufacturerProduct"),
                anyAppMethodCallFrame(JpaCascadeCrudService.class, "getManufacturerById"),
                anyProxyMethodCallFrame(ManufacturerRepository.class, "findById"),
                anyThirdPartyMethodCallFrameOnClassAssignableFrom(EntityManager.class, "find"),
                anyThirdPartyMethodCallFrameOnClassAssignableFrom(PreparedStatement.class, "executeQuery")
        )));

        OperationNodeView operationNodeView2 = sessionNode.getOperations().get(1);
        assertThat(operationNodeView2, notNullValue());
        assertThat(operationNodeView2.getOperationType(), equalTo(OperationType.IMPLICIT));
        assertThat(operationNodeView2.getStatements(), hasSize(equalTo(1)));
        assertThat(operationNodeView2.getLazyInitialisation(), notNullValue());
        assertThat(operationNodeView2.getLazyInitialisation().getEntityClassName(), equalTo(Manufacturer.class.getName()));
        assertThat(operationNodeView2.getLazyInitialisation().getFieldName(), equalTo("products"));
        assertThat(operationNodeView2.getLazyInitialisation().getType(), equalTo(LazyInitialisationType.COLLECTION));
        assertThat(operationNodeView2.getCallFramesStack(), frameCallSequenceMatcher(List.of(
                anyAppMethodCallFrame(JpaCascadeCrudService.class, "deleteManufacturerProduct"),
                anyAppMethodCallFrame(Manufacturer.class, "deleteProduct"),
                anyAppMethodCallFrame(Manufacturer.class, "findProductByName"),
                anyThirdPartyMethodCallFrameOnClassAssignableFrom(Session.class, "initializeCollection"),
                anyThirdPartyMethodCallFrameOnClassAssignableFrom(PreparedStatement.class, "executeQuery")
        )));

        OperationNodeView operationNodeView3 = sessionNode.getOperations().get(2);
        assertThat(operationNodeView3, notNullValue());
        assertThat(operationNodeView3.getOperationType(), equalTo(OperationType.EXPLICIT)); // TODO: why it's explicit?
        assertThat(operationNodeView3.getStatements(), hasSize(equalTo(1)));
        assertThat(operationNodeView3.getLazyInitialisation(), nullValue());
        assertThat(operationNodeView3.getCallFramesStack(), allFrameCallMatcher(notAppMethodCallFrame()));
        assertThat(operationNodeView3.getCallFramesStack(), frameCallSequenceMatcher(List.of(
                anyThirdPartyMethodCallFrameOnClassAssignableFrom(EntityTransaction.class, "commit"),
                anyThirdPartyMethodCallFrame(SessionImpl.class, "doFlush"),
                anyThirdPartyMethodCallFrameOnClassAssignableFrom(PreparedStatement.class, "executeUpdate")
        )));

        StatementNodeView statementNodeView1 = operationNodeView1.getStatements().get(0);
        assertThat(statementNodeView1, notNullValue());
        assertThat(statementNodeView1.getStatementType(), equalTo(StatementType.SELECT));
        assertThat(statementNodeView1.getSql(), endsWith(String.format(
                "from manufacturer manufactur0_ where manufactur0_.id=%d", MANUFACTURER_ID)));

        StatementNodeView statementNodeView2 = operationNodeView2.getStatements().get(0);
        assertThat(statementNodeView2, notNullValue());
        assertThat(statementNodeView2.getStatementType(), equalTo(StatementType.SELECT));
        assertThat(statementNodeView2.getSql(), endsWith(String.format(
                "from product products0_ where products0_.manufacturer_id=%d", MANUFACTURER_ID)));

        StatementNodeView statementNodeView3 = operationNodeView3.getStatements().get(0);
        assertThat(statementNodeView3, notNullValue());
        assertThat(statementNodeView3.getStatementType(), equalTo(StatementType.DELETE));
        assertThat(statementNodeView3.getSql(), equalTo(String.format(
                "delete from product where id=%d", product.getId())));

    }

}