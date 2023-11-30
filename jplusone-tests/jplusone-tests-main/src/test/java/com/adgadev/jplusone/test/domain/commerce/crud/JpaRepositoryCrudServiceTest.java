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

package com.adgadev.jplusone.test.domain.commerce.crud;

import com.adgadev.jplusone.core.registry.OperationNodeView;
import com.adgadev.jplusone.core.registry.OperationType;
import com.adgadev.jplusone.core.registry.RootNodeView;
import com.adgadev.jplusone.core.registry.SessionNodeView;
import com.adgadev.jplusone.core.registry.StatementNodeView;
import com.adgadev.jplusone.core.registry.StatementType;
import com.adgadev.jplusone.test.domain.commerce.Manufacturer;
import com.adgadev.jplusone.test.matchers.JPlusOneMatchers;
import com.adgadev.jplusone.test.matchers.frame.FrameExtractSpecification;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.internal.SessionImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.transaction.Transactional;
import java.sql.PreparedStatement;
import java.util.List;

import static com.adgadev.jplusone.core.registry.LazyInitialisation.collectionLazyInitialisation;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Slf4j
@ActiveProfiles("integration-test")
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class JpaRepositoryCrudServiceTest {

    private static final Long NEW_ID = 10L;

    private static final String MANUFACTURER_NAME = "Original manufacturer name";

    @Autowired
    private JpaRepositoryCrudService crudService;

    @Autowired
    private RootNodeView rootNode;

    @Test
    void shouldAddManufacturer() {
        // when
        crudService.addManufacturer(NEW_ID, MANUFACTURER_NAME);

        // then
        assertThat(rootNode.getSessions(), hasSize(greaterThan(0)));

        SessionNodeView sessionNode = rootNode.getSessions().get(rootNode.getSessions().size() - 1);
        assertThat(sessionNode, notNullValue());
        assertThat(sessionNode.getOperations(), hasSize(equalTo(2)));

        assertThat(sessionNode.getSessionFrameStack(), JPlusOneMatchers.frameCallSequenceMatcher(List.of(
                FrameExtractSpecification.anyAppMethodCallFrame(JpaRepositoryCrudServiceTest.class, "shouldAddManufacturer"),
                FrameExtractSpecification.anyProxyMethodCallFrame(JpaRepositoryCrudService.class, "addManufacturer")
        )));

        OperationNodeView operationNodeView1 = sessionNode.getOperations().get(0);
        assertThat(operationNodeView1, notNullValue());
        assertThat(operationNodeView1.getOperationType(), equalTo(OperationType.EXPLICIT));
        assertThat(operationNodeView1.getStatements(), hasSize(equalTo(1)));
        assertThat(operationNodeView1.getLazyInitialisations(), empty());
        assertThat(operationNodeView1.getCallFramesStack(), JPlusOneMatchers.frameCallSequenceMatcher(List.of(
                FrameExtractSpecification.anyAppMethodCallFrame(JpaRepositoryCrudService.class, "addManufacturer"),
                FrameExtractSpecification.anyProxyMethodCallFrame(ManufacturerRepository.class, "save"),
                FrameExtractSpecification.anyThirdPartyMethodCallFrameOnClassAssignableFrom(EntityManager.class, "merge"),
                FrameExtractSpecification.anyThirdPartyMethodCallFrameOnClassAssignableFrom(PreparedStatement.class, "executeQuery")
        )));

        OperationNodeView operationNodeView2 = sessionNode.getOperations().get(1);
        assertThat(operationNodeView2, notNullValue());
        assertThat(operationNodeView2.getOperationType(), equalTo(OperationType.COMMIT));
        assertThat(operationNodeView2.getStatements(), hasSize(equalTo(1)));
        assertThat(operationNodeView2.getLazyInitialisations(), empty());
        assertThat(operationNodeView2.getCallFramesStack(), JPlusOneMatchers.allFrameCallMatcher(FrameExtractSpecification.notAppMethodCallFrame()));
        assertThat(operationNodeView2.getCallFramesStack(), JPlusOneMatchers.frameCallSequenceMatcher(List.of(
                FrameExtractSpecification.anyThirdPartyMethodCallFrameOnClassAssignableFrom(EntityTransaction.class, "commit"),
                FrameExtractSpecification.anyThirdPartyMethodCallFrame(SessionImpl.class, "doFlush"),
                FrameExtractSpecification.anyThirdPartyMethodCallFrameOnClassAssignableFrom(PreparedStatement.class, "executeUpdate")
        )));

        StatementNodeView statementNodeView1 = operationNodeView1.getStatements().get(0);
        assertThat(statementNodeView1, notNullValue());
        assertThat(statementNodeView1.getStatementType(), equalTo(StatementType.SELECT));
        assertThat(statementNodeView1.getSql(), endsWith(String.format(
                "from manufacturer m1_0 " +
                        "left join product p1_0 on m1_0.id=p1_0.manufacturer_id " +
                        "where m1_0.id=%d", NEW_ID)));

        StatementNodeView statementNodeView = operationNodeView2.getStatements().get(0);
        assertThat(statementNodeView, notNullValue());
        assertThat(statementNodeView.getStatementType(), equalTo(StatementType.INSERT));
        assertThat(statementNodeView.getSql(), equalTo(String.format(
                "insert into manufacturer (name, id) values ('%s', %d)", MANUFACTURER_NAME, NEW_ID)));
    }

    @Test
    void shouldUpdateManufacturerNameByMerge() {
        // given
        crudService.addManufacturer(NEW_ID + 1, MANUFACTURER_NAME);

        // when
        crudService.updateManufacturerNameByMerge(NEW_ID + 1, "New manufacturer name");

        // then
        assertThat(rootNode.getSessions(), hasSize(greaterThan(0)));

        SessionNodeView sessionNode = rootNode.getSessions().get(rootNode.getSessions().size() - 1);
        assertThat(sessionNode, notNullValue());
        assertThat(sessionNode.getOperations(), hasSize(equalTo(2)));
        assertThat(sessionNode.getSessionFrameStack(), JPlusOneMatchers.frameCallSequenceMatcher(List.of(
                FrameExtractSpecification.anyAppMethodCallFrame(JpaRepositoryCrudServiceTest.class, "shouldUpdateManufacturerNameByMerge"),
                FrameExtractSpecification.anyProxyMethodCallFrame(JpaRepositoryCrudService.class, "updateManufacturerNameByMerge")
        )));

        OperationNodeView operationNodeView1 = sessionNode.getOperations().get(0);
        assertThat(operationNodeView1, notNullValue());
        assertThat(operationNodeView1.getOperationType(), equalTo(OperationType.EXPLICIT));
        assertThat(operationNodeView1.getStatements(), hasSize(equalTo(1)));
        assertThat(operationNodeView1.getLazyInitialisations(), empty());
        assertThat(operationNodeView1.getCallFramesStack(), JPlusOneMatchers.frameCallSequenceMatcher(List.of(
                FrameExtractSpecification.anyAppMethodCallFrame(JpaRepositoryCrudService.class, "updateManufacturerNameByMerge"),
                FrameExtractSpecification.anyThirdPartyMethodCallFrameOnClassAssignableFrom(EntityManager.class, "merge"),
                FrameExtractSpecification.anyThirdPartyMethodCallFrameOnClassAssignableFrom(PreparedStatement.class, "executeQuery")
        )));

        OperationNodeView operationNodeView2 = sessionNode.getOperations().get(1);
        assertThat(operationNodeView2, notNullValue());
        assertThat(operationNodeView2.getOperationType(), equalTo(OperationType.COMMIT));
        assertThat(operationNodeView2.getStatements(), hasSize(equalTo(1)));
        assertThat(operationNodeView2.getLazyInitialisations(), empty());
        assertThat(operationNodeView2.getCallFramesStack(), JPlusOneMatchers.allFrameCallMatcher(FrameExtractSpecification.notAppMethodCallFrame()));
        assertThat(operationNodeView2.getCallFramesStack(), JPlusOneMatchers.frameCallSequenceMatcher(List.of(
                FrameExtractSpecification.anyThirdPartyMethodCallFrameOnClassAssignableFrom(EntityTransaction.class, "commit"),
                FrameExtractSpecification.anyThirdPartyMethodCallFrame(SessionImpl.class, "doFlush"),
                FrameExtractSpecification.anyThirdPartyMethodCallFrameOnClassAssignableFrom(PreparedStatement.class, "executeUpdate")
        )));

        StatementNodeView statementNodeView1 = operationNodeView1.getStatements().get(0);
        assertThat(statementNodeView1, notNullValue());
        assertThat(statementNodeView1.getStatementType(), equalTo(StatementType.SELECT));
        assertThat(statementNodeView1.getSql(), endsWith(String.format(
                "from manufacturer m1_0 " +
                        "left join product p1_0 on m1_0.id=p1_0.manufacturer_id " +
                        "where m1_0.id=%d", NEW_ID + 1)));

        StatementNodeView statementNodeView2 = operationNodeView2.getStatements().get(0);
        assertThat(statementNodeView2, notNullValue());
        assertThat(statementNodeView2.getStatementType(), equalTo(StatementType.UPDATE));
        assertThat(statementNodeView2.getSql(), equalTo(String.format(
                "update manufacturer set name='New manufacturer name' where id=%d", NEW_ID + 1)));
    }

    @Test
    void shouldUpdateManufacturerNameOnManagedEntity() {
        // given
        crudService.addManufacturer(NEW_ID + 2,  MANUFACTURER_NAME);

        // when
        crudService.updateManufacturerNameOnManagedEntity(NEW_ID + 2, "New manufacturer name");

        // then
        assertThat(rootNode.getSessions(), hasSize(greaterThan(0)));

        SessionNodeView sessionNode = rootNode.getSessions().get(rootNode.getSessions().size() - 1);
        assertThat(sessionNode, notNullValue());
        assertThat(sessionNode.getOperations(), hasSize(equalTo(2)));
        assertThat(sessionNode.getSessionFrameStack(), JPlusOneMatchers.frameCallSequenceMatcher(List.of(
                FrameExtractSpecification.anyAppMethodCallFrame(JpaRepositoryCrudServiceTest.class, "shouldUpdateManufacturerNameOnManagedEntity"),
                FrameExtractSpecification.anyProxyMethodCallFrame(JpaRepositoryCrudService.class, "updateManufacturerNameOnManagedEntity")
        )));

        OperationNodeView operationNodeView1 = sessionNode.getOperations().get(0);
        assertThat(operationNodeView1, notNullValue());
        assertThat(operationNodeView1.getOperationType(), equalTo(OperationType.EXPLICIT));
        assertThat(operationNodeView1.getStatements(), hasSize(equalTo(1)));
        assertThat(operationNodeView1.getLazyInitialisations(), empty());
        assertThat(operationNodeView1.getCallFramesStack(), JPlusOneMatchers.frameCallSequenceMatcher(List.of(
                FrameExtractSpecification.anyAppMethodCallFrame(JpaRepositoryCrudService.class, "updateManufacturerNameOnManagedEntity"),
                FrameExtractSpecification.anyThirdPartyMethodCallFrameOnClassAssignableFrom(EntityManager.class, "find"),
                FrameExtractSpecification.anyThirdPartyMethodCallFrameOnClassAssignableFrom(PreparedStatement.class, "executeQuery")
        )));

        OperationNodeView operationNodeView2 = sessionNode.getOperations().get(1);
        assertThat(operationNodeView2, notNullValue());
        assertThat(operationNodeView2.getOperationType(), equalTo(OperationType.COMMIT));
        assertThat(operationNodeView2.getStatements(), hasSize(equalTo(1)));
        assertThat(operationNodeView2.getLazyInitialisations(), empty());
        assertThat(operationNodeView2.getCallFramesStack(), JPlusOneMatchers.allFrameCallMatcher(FrameExtractSpecification.notAppMethodCallFrame()));
        assertThat(operationNodeView2.getCallFramesStack(), JPlusOneMatchers.frameCallSequenceMatcher(List.of(
                FrameExtractSpecification.anyThirdPartyMethodCallFrameOnClassAssignableFrom(EntityTransaction.class, "commit"),
                FrameExtractSpecification.anyThirdPartyMethodCallFrame(SessionImpl.class, "doFlush"),
                FrameExtractSpecification.anyThirdPartyMethodCallFrameOnClassAssignableFrom(PreparedStatement.class, "executeUpdate")
        )));

        StatementNodeView statementNodeView1 = operationNodeView1.getStatements().get(0);
        assertThat(statementNodeView1, notNullValue());
        assertThat(statementNodeView1.getStatementType(), equalTo(StatementType.SELECT));
        assertThat(statementNodeView1.getSql(), endsWith(String.format(
                "from manufacturer m1_0 where m1_0.id=%d", NEW_ID + 2)));

        StatementNodeView statementNodeView2 = operationNodeView2.getStatements().get(0);
        assertThat(statementNodeView2, notNullValue());
        assertThat(statementNodeView2.getStatementType(), equalTo(StatementType.UPDATE));
        assertThat(statementNodeView2.getSql(), equalTo(String.format(
                "update manufacturer set name='New manufacturer name' where id=%d", NEW_ID + 2)));
    }

    @Test
    void shouldDeleteManufacturer() {
        // given
        crudService.addManufacturer(NEW_ID + 3,  MANUFACTURER_NAME);

        // when
        crudService.deleteManufacturer(NEW_ID + 3);

        // then
        assertThat(rootNode.getSessions(), hasSize(greaterThan(0)));

        SessionNodeView sessionNode = rootNode.getSessions().get(rootNode.getSessions().size() - 1);
        assertThat(sessionNode, notNullValue());
        assertThat(sessionNode.getOperations(), hasSize(equalTo(3)));
        assertThat(sessionNode.getSessionFrameStack(), JPlusOneMatchers.frameCallSequenceMatcher(List.of(
                FrameExtractSpecification.anyAppMethodCallFrame(JpaRepositoryCrudServiceTest.class, "shouldDeleteManufacturer"),
                FrameExtractSpecification.anyProxyMethodCallFrame(JpaRepositoryCrudService.class, "deleteManufacturer")
        )));

        OperationNodeView operationNodeView1 = sessionNode.getOperations().get(0);
        assertThat(operationNodeView1, notNullValue());
        assertThat(operationNodeView1.getOperationType(), equalTo(OperationType.EXPLICIT));
        assertThat(operationNodeView1.getStatements(), hasSize(equalTo(1)));
        assertThat(operationNodeView1.getLazyInitialisations(), empty());
        assertThat(operationNodeView1.getCallFramesStack(), JPlusOneMatchers.frameCallSequenceMatcher(List.of(
                FrameExtractSpecification.anyAppMethodCallFrame(JpaRepositoryCrudService.class, "deleteManufacturer"),
                FrameExtractSpecification.anyThirdPartyMethodCallFrameOnClassAssignableFrom(EntityManager.class, "find"),
                FrameExtractSpecification.anyThirdPartyMethodCallFrameOnClassAssignableFrom(PreparedStatement.class, "executeQuery")
        )));

        OperationNodeView operationNodeView2 = sessionNode.getOperations().get(1);
        assertThat(operationNodeView2, notNullValue());
        assertThat(operationNodeView2.getOperationType(), equalTo(OperationType.IMPLICIT));
        assertThat(operationNodeView2.getStatements(), hasSize(equalTo(1)));
        assertThat(operationNodeView2.getLazyInitialisations(), contains(collectionLazyInitialisation(Manufacturer.class.getName(), "products")));
        assertThat(operationNodeView2.getCallFramesStack(), JPlusOneMatchers.frameCallSequenceMatcher(List.of(
                FrameExtractSpecification.anyAppMethodCallFrame(JpaRepositoryCrudService.class, "deleteManufacturer"),
                FrameExtractSpecification.anyThirdPartyMethodCallFrameOnClassAssignableFrom(EntityManager.class, "remove"),
                FrameExtractSpecification.anyThirdPartyMethodCallFrameOnClassAssignableFrom(PreparedStatement.class, "executeQuery")
        )));

        OperationNodeView operationNodeView3 = sessionNode.getOperations().get(2);
        assertThat(operationNodeView3, notNullValue());
        assertThat(operationNodeView3.getOperationType(), equalTo(OperationType.COMMIT));
        assertThat(operationNodeView3.getStatements(), hasSize(equalTo(1)));
        assertThat(operationNodeView3.getLazyInitialisations(), empty());
        assertThat(operationNodeView3.getCallFramesStack(), JPlusOneMatchers.allFrameCallMatcher(FrameExtractSpecification.notAppMethodCallFrame()));
        assertThat(operationNodeView3.getCallFramesStack(), JPlusOneMatchers.frameCallSequenceMatcher(List.of(
                FrameExtractSpecification.anyThirdPartyMethodCallFrameOnClassAssignableFrom(EntityTransaction.class, "commit"),
                FrameExtractSpecification.anyThirdPartyMethodCallFrame(SessionImpl.class, "doFlush"),
                FrameExtractSpecification.anyThirdPartyMethodCallFrameOnClassAssignableFrom(PreparedStatement.class, "executeUpdate")
        )));

        StatementNodeView statementNodeView1 = operationNodeView1.getStatements().get(0);
        assertThat(statementNodeView1, notNullValue());
        assertThat(statementNodeView1.getStatementType(), equalTo(StatementType.SELECT));
        assertThat(statementNodeView1.getSql(), endsWith(String.format(
                "from manufacturer m1_0 where m1_0.id=%d", NEW_ID + 3)));

        StatementNodeView statementNodeView2 = operationNodeView2.getStatements().get(0);
        assertThat(statementNodeView2, notNullValue());
        assertThat(statementNodeView2.getStatementType(), equalTo(StatementType.SELECT));
        assertThat(statementNodeView2.getSql(), endsWith(String.format(
                "from product p1_0 where p1_0.manufacturer_id=%d", NEW_ID + 3)));

        StatementNodeView statementNodeView3 = operationNodeView3.getStatements().get(0);
        assertThat(statementNodeView3, notNullValue());
        assertThat(statementNodeView3.getStatementType(), equalTo(StatementType.DELETE));
        assertThat(statementNodeView3.getSql(), equalTo(String.format(
                "delete from manufacturer where id=%d", NEW_ID + 3)));
    }

    @Test
    void shouldDeleteManufacturerById() {
        // given
        crudService.addManufacturer(NEW_ID + 4,  MANUFACTURER_NAME);

        // when
        crudService.deleteManufacturerById(NEW_ID + 4);

        // then
        assertThat(rootNode.getSessions(), hasSize(greaterThan(0)));

        SessionNodeView sessionNode = rootNode.getSessions().get(rootNode.getSessions().size() - 1);
        assertThat(sessionNode, notNullValue());
        assertThat(sessionNode.getOperations(), hasSize(equalTo(3)));
        assertThat(sessionNode.getSessionFrameStack(), JPlusOneMatchers.frameCallSequenceMatcher(List.of(
                FrameExtractSpecification.anyAppMethodCallFrame(JpaRepositoryCrudServiceTest.class, "shouldDeleteManufacturerById"),
                FrameExtractSpecification.anyProxyMethodCallFrame(JpaRepositoryCrudService.class, "deleteManufacturerById")
        )));

        OperationNodeView operationNodeView1 = sessionNode.getOperations().get(0);
        assertThat(operationNodeView1, notNullValue());
        assertThat(operationNodeView1.getOperationType(), equalTo(OperationType.EXPLICIT));
        assertThat(operationNodeView1.getStatements(), hasSize(equalTo(1)));
        assertThat(operationNodeView1.getLazyInitialisations(), empty());
        assertThat(operationNodeView1.getCallFramesStack(), JPlusOneMatchers.frameCallSequenceMatcher(List.of(
                FrameExtractSpecification.anyAppMethodCallFrame(JpaRepositoryCrudService.class, "deleteManufacturerById"),
                FrameExtractSpecification.anyProxyMethodCallFrame(ManufacturerRepository.class, "deleteById"),
                FrameExtractSpecification.anyThirdPartyMethodCallFrameOnClassAssignableFrom(EntityManager.class, "find"),
                FrameExtractSpecification.anyThirdPartyMethodCallFrameOnClassAssignableFrom(PreparedStatement.class, "executeQuery")
        )));

        OperationNodeView operationNodeView2 = sessionNode.getOperations().get(1);
        assertThat(operationNodeView2, notNullValue());
        assertThat(operationNodeView2.getOperationType(), equalTo(OperationType.IMPLICIT));
        assertThat(operationNodeView2.getStatements(), hasSize(equalTo(1)));
        assertThat(operationNodeView2.getLazyInitialisations(), contains(collectionLazyInitialisation(Manufacturer.class.getName(), "products")));
        assertThat(operationNodeView2.getCallFramesStack(), JPlusOneMatchers.frameCallSequenceMatcher(List.of(
                FrameExtractSpecification.anyAppMethodCallFrame(JpaRepositoryCrudService.class, "deleteManufacturerById"),
                FrameExtractSpecification.anyProxyMethodCallFrame(ManufacturerRepository.class, "deleteById"),
                FrameExtractSpecification.anyThirdPartyMethodCallFrameOnClassAssignableFrom(EntityManager.class, "remove"),
                FrameExtractSpecification.anyThirdPartyMethodCallFrameOnClassAssignableFrom(PreparedStatement.class, "executeQuery")
        )));

        OperationNodeView operationNodeView3 = sessionNode.getOperations().get(2);
        assertThat(operationNodeView3, notNullValue());
        assertThat(operationNodeView3.getOperationType(), equalTo(OperationType.COMMIT));
        assertThat(operationNodeView3.getStatements(), hasSize(equalTo(1)));
        assertThat(operationNodeView3.getLazyInitialisations(), empty());
        assertThat(operationNodeView3.getCallFramesStack(), JPlusOneMatchers.allFrameCallMatcher(FrameExtractSpecification.notAppMethodCallFrame()));
        assertThat(operationNodeView3.getCallFramesStack(), JPlusOneMatchers.frameCallSequenceMatcher(List.of(
                FrameExtractSpecification.anyThirdPartyMethodCallFrameOnClassAssignableFrom(EntityTransaction.class, "commit"),
                FrameExtractSpecification.anyThirdPartyMethodCallFrame(SessionImpl.class, "doFlush"),
                FrameExtractSpecification.anyThirdPartyMethodCallFrameOnClassAssignableFrom(PreparedStatement.class, "executeUpdate")
        )));

        StatementNodeView statementNodeView1 = operationNodeView1.getStatements().get(0);
        assertThat(statementNodeView1, notNullValue());
        assertThat(statementNodeView1.getStatementType(), equalTo(StatementType.SELECT));
        assertThat(statementNodeView1.getSql(), endsWith(String.format(
                "from manufacturer m1_0 where m1_0.id=%d", NEW_ID + 4)));

        StatementNodeView statementNodeView2 = operationNodeView2.getStatements().get(0);
        assertThat(statementNodeView2, notNullValue());
        assertThat(statementNodeView2.getStatementType(), equalTo(StatementType.SELECT));
        assertThat(statementNodeView2.getSql(), endsWith(String.format(
                "from product p1_0 where p1_0.manufacturer_id=%d", NEW_ID + 4)));

        StatementNodeView statementNodeView3 = operationNodeView3.getStatements().get(0);
        assertThat(statementNodeView3, notNullValue());
        assertThat(statementNodeView3.getStatementType(), equalTo(StatementType.DELETE));
        assertThat(statementNodeView3.getSql(), equalTo(String.format(
                "delete from manufacturer where id=%d", NEW_ID + 4)));
    }

    @Test
    @Transactional
    void shouldSetupDataAndDeleteManufacturerInTestTransaction() {
        // given
        int initialSessionsAmount = rootNode.getSessions().size();
        crudService.addManufacturer(NEW_ID + 5, MANUFACTURER_NAME);

        // when
        crudService.deleteManufacturerById(NEW_ID + 5);

        // then
        assertThat(rootNode.getSessions(), hasSize(equalTo(initialSessionsAmount)));
    }
}