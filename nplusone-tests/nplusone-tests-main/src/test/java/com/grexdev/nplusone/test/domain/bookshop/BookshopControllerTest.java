package com.grexdev.nplusone.test.domain.bookshop;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.equalTo;
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

    @Test
    void shouldGetBookDetailsLazily() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .get("/book/lazy")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", equalTo("Godfather")))
                .andExpect(jsonPath("$.author", equalTo("Mario Puzo")));
    }

    @Test
    void shouldGetBookDetailsEagerly() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .get("/book/eager")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", equalTo("Godfather")))
                .andExpect(jsonPath("$.author", equalTo("Mario Puzo")));
    }

}