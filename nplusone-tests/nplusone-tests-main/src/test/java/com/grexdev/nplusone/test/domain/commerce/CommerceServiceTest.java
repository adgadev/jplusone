package com.grexdev.nplusone.test.domain.commerce;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;


@ActiveProfiles("integration-test")
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class CommerceServiceTest {

    @Autowired
    private CommerceService commerceService;

    @Test
    void test1() {
        commerceService.getClient();
    }

}