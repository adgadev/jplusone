package com.grexdev.nplusone.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class NPlusOneTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(NPlusOneTestApplication.class, args);
    }

}
