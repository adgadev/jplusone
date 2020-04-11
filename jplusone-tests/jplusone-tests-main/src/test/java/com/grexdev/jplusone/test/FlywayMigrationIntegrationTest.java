package com.grexdev.jplusone.test;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;

@Transactional
@ActiveProfiles("integration-test")
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class FlywayMigrationIntegrationTest {

    @Autowired
    private Flyway flyway;

    @Test
    public void shouldRunFlywayMigrationAgain() {
        flyway.clean();
        flyway.migrate();
    }

}
