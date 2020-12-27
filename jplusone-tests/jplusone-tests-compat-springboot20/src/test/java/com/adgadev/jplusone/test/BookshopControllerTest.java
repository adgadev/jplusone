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

package com.adgadev.jplusone.test;

import com.adgadev.jplusone.core.report.ReportGenerator;
import nl.altindag.log.LogCaptor;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("integration-test")
@SpringBootTest(webEnvironment = MOCK, classes = TestDomainApplication.class)
@AutoConfigureMockMvc
class BookshopControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ResourceLoader resourceLoader;

    @Test
    void shouldGetBookDetailsLazily() throws Exception {
        LogCaptor reportLogCaptor = LogCaptor.forClass(ReportGenerator.class);

        mvc.perform(MockMvcRequestBuilders
                .get("/book/lazy")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", equalTo("Godfather")))
                .andExpect(jsonPath("$.author", equalTo("Mario Puzo")));


        String expectedLog = loadTextFileFromClasspath("expected-reports/book-lazy-report.txt")
                .replace("#LINE_NUMBER#", "62");
        assertThat(reportLogCaptor.getDebugLogs(), hasSize(1));
        assertThat(reportLogCaptor.getDebugLogs().get(0), equalTo(expectedLog));
    }

    @Test
    void shouldGetBookDetailsEagerly() throws Exception {
        LogCaptor reportLogCaptor = LogCaptor.forClass(ReportGenerator.class);

        mvc.perform(MockMvcRequestBuilders
                .get("/book/eager")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", equalTo("Godfather")))
                .andExpect(jsonPath("$.author", equalTo("Mario Puzo")));

        String expectedLog = loadTextFileFromClasspath("expected-reports/book-eager-report.txt")
                .replace("#LINE_NUMBER#", "81");
        assertThat(reportLogCaptor.getDebugLogs(), hasSize(1));
        assertThat(reportLogCaptor.getDebugLogs().get(0), equalTo(expectedLog));
    }

    private String loadTextFileFromClasspath(String path) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:" + path);
        return IOUtils.toString(resource.getInputStream(), UTF_8);
    }
}