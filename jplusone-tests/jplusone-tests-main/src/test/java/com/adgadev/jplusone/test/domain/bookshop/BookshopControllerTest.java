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

package com.adgadev.jplusone.test.domain.bookshop;

import com.adgadev.jplusone.core.registry.OperationNodeView;
import com.adgadev.jplusone.core.registry.OperationType;
import com.adgadev.jplusone.core.registry.RootNodeView;
import com.adgadev.jplusone.core.registry.SessionNodeView;
import com.adgadev.jplusone.core.registry.StatementNodeView;
import com.adgadev.jplusone.core.registry.StatementType;
import com.adgadev.jplusone.test.matchers.JPlusOneMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.persistence.EntityManager;
import java.util.List;

import static com.adgadev.jplusone.core.registry.LazyInitialisation.entityLazyInitialisation;
import static com.adgadev.jplusone.test.matchers.frame.FrameExtractSpecification.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("integration-test")
@SpringBootTest(webEnvironment = MOCK)
@AutoConfigureMockMvc
class BookshopControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private RootNodeView rootNode;

    @Test
    void shouldGetBookDetailsLazily() throws Exception {
        // given
        int sessionAmount = rootNode.getSessions().size();

        // when
        mvc.perform(MockMvcRequestBuilders
                .get("/book/lazy")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", equalTo("Godfather")))
                .andExpect(jsonPath("$.author", equalTo("Mario Puzo")));

        // then
        assertThat(rootNode.getSessions(), hasSize(equalTo(sessionAmount + 1)));
        SessionNodeView sessionNode = rootNode.getSessions().get(rootNode.getSessions().size() - 1);
        assertThat(sessionNode, notNullValue());
        assertThat(sessionNode.getOperations(), hasSize(equalTo(2)));
        assertThat(sessionNode.getSessionFrameStack(), JPlusOneMatchers.frameCallSequenceMatcher(List.of(
                anyAppMethodCallFrame(BookshopControllerTest.class, "shouldGetBookDetailsLazily")
        )));

        OperationNodeView operationNodeView1 = sessionNode.getOperations().get(0);
        assertThat(operationNodeView1, notNullValue());
        assertThat(operationNodeView1.getOperationType(), equalTo(OperationType.EXPLICIT));
        assertThat(operationNodeView1.getStatements(), hasSize(1));
        assertThat(operationNodeView1.getLazyInitialisations(), empty());
        assertThat(operationNodeView1.getCallFramesStack(), JPlusOneMatchers.frameCallSequenceMatcher(List.of(
                anyAppMethodCallFrame(BookshopController.class, "getSampleBookUsingLazyLoading"),
                anyProxyMethodCallFrame(BookshopService.class, "getSampleBookDetailsUsingLazyLoading"),
                anyAppMethodCallFrame(BookshopService.class, "getSampleBookDetailsUsingLazyLoading"),
                anyProxyMethodCallFrame(BookRepository.class, "findById"),
                anyThirdPartyMethodCallFrameOnClassAssignableFrom(EntityManager.class, "find")
        )));

        OperationNodeView operationNodeView2 = sessionNode.getOperations().get(1);
        assertThat(operationNodeView2, notNullValue());
        assertThat(operationNodeView2.getOperationType(), equalTo(OperationType.IMPLICIT));
        assertThat(operationNodeView2.getStatements(), hasSize(1));
        assertThat(operationNodeView2.getLazyInitialisations(), contains(entityLazyInitialisation(Author.class.getName())));
        assertThat(operationNodeView2.getCallFramesStack(), JPlusOneMatchers.frameCallSequenceMatcher(List.of(
                anyAppMethodCallFrame(BookshopController.class, "getSampleBookUsingLazyLoading"),
                anyProxyMethodCallFrame(BookshopService.class, "getSampleBookDetailsUsingLazyLoading"),
                anyAppMethodCallFrame(BookshopService.class, "getSampleBookDetailsUsingLazyLoading"),
                anyProxyMethodCallFrame(Author.class, "getName")
        )));

        StatementNodeView statementNodeView1 = operationNodeView1.getStatements().get(0);
        assertThat(statementNodeView1, notNullValue());
        assertThat(statementNodeView1.getStatementType(), equalTo(StatementType.SELECT));
        assertThat(statementNodeView1.getSql(), endsWith("from book book0_ where book0_.id=1"));

        StatementNodeView statementNodeView2 = operationNodeView2.getStatements().get(0);
        assertThat(statementNodeView2, notNullValue());
        assertThat(statementNodeView2.getStatementType(), equalTo(StatementType.SELECT));
        assertThat(statementNodeView2.getSql(), endsWith("from author author0_ left outer join genre genre1_ on author0_.genre_id=genre1_.id where author0_.id=1"));
    }

    @Test
    void shouldGetBookDetailsEagerly() throws Exception {
        // given
        int sessionAmount = rootNode.getSessions().size();

        // when
        mvc.perform(MockMvcRequestBuilders
                .get("/book/eager")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", equalTo("Godfather")))
                .andExpect(jsonPath("$.author", equalTo("Mario Puzo")));

        // then
        assertThat(rootNode.getSessions(), hasSize(equalTo(sessionAmount + 1)));
        SessionNodeView sessionNode = rootNode.getSessions().get(rootNode.getSessions().size() - 1);
        assertThat(sessionNode, notNullValue());
        assertThat(sessionNode.getOperations(), hasSize(equalTo(2)));
        assertThat(sessionNode.getSessionFrameStack(), JPlusOneMatchers.frameCallSequenceMatcher(List.of(
                anyAppMethodCallFrame(BookshopControllerTest.class, "shouldGetBookDetailsEagerly")
        )));

        OperationNodeView operationNodeView1 = sessionNode.getOperations().get(0);
        assertThat(operationNodeView1, notNullValue());
        assertThat(operationNodeView1.getOperationType(), equalTo(OperationType.EXPLICIT));
        assertThat(operationNodeView1.getStatements(), hasSize(1));
        assertThat(operationNodeView1.getLazyInitialisations(), empty());
        assertThat(operationNodeView1.getCallFramesStack(), JPlusOneMatchers.frameCallSequenceMatcher(List.of(
                anyAppMethodCallFrame(BookshopController.class, "getSampleBookUsingEagerLoading"),
                anyProxyMethodCallFrame(BookshopService.class, "getSampleBookDetailsUsingEagerLoading"),
                anyAppMethodCallFrame(BookshopService.class, "getSampleBookDetailsUsingEagerLoading"),
                anyProxyMethodCallFrame(BookRepository.class, "findByIdAndFetchAuthor")
        )));

        OperationNodeView operationNodeView2 = sessionNode.getOperations().get(1);
        assertThat(operationNodeView2, notNullValue());
        assertThat(operationNodeView2.getOperationType(), equalTo(OperationType.EXPLICIT));
        assertThat(operationNodeView2.getStatements(), hasSize(1));
        assertThat(operationNodeView2.getLazyInitialisations(), empty());
        assertThat(operationNodeView2.getCallFramesStack(), JPlusOneMatchers.frameCallSequenceMatcher(List.of(
                anyAppMethodCallFrame(BookshopController.class, "getSampleBookUsingEagerLoading"),
                anyProxyMethodCallFrame(BookshopService.class, "getSampleBookDetailsUsingEagerLoading"),
                anyAppMethodCallFrame(BookshopService.class, "getSampleBookDetailsUsingEagerLoading"),
                anyProxyMethodCallFrame(BookRepository.class, "findByIdAndFetchAuthor")  // TODO: shouldn't it be under one operation?
        )));

        StatementNodeView statementNodeView1 = operationNodeView1.getStatements().get(0);
        assertThat(statementNodeView1, notNullValue());
        assertThat(statementNodeView1.getStatementType(), equalTo(StatementType.SELECT));
        assertThat(statementNodeView1.getSql(), endsWith("from book book0_ left outer join author author1_ on book0_.author_id=author1_.id where book0_.id=1"));

        StatementNodeView statementNodeView2 = operationNodeView2.getStatements().get(0);
        assertThat(statementNodeView2, notNullValue());
        assertThat(statementNodeView2.getStatementType(), equalTo(StatementType.SELECT));
        assertThat(statementNodeView2.getSql(), endsWith("from genre genre0_ where genre0_.id=1"));
    }
}