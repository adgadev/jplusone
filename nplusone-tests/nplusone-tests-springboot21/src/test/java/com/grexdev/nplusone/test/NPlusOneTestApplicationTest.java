package com.grexdev.nplusone.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@Slf4j
@ActiveProfiles("integration-test")
@SpringBootTest
class NPlusOneTestApplicationTest {

    @Test
    void contextLoads() {
    }

}