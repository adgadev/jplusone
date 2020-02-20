package com.grexdev.nplusone.test.domain1;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class Domain1ServiceIntegrationTest {

    @Autowired
    private Domain1Service service;

    @Test
    void shouldTest() {
        assertEquals(asList("first-object-a", "first-object-b"), service.getDataFromAAndFetchDataFromB());
    }

}