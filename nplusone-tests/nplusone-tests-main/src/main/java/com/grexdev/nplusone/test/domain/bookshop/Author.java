package com.grexdev.nplusone.test.domain.bookshop;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Entity;
import javax.persistence.Id;

@Slf4j
@Getter
@Entity
class Author {

    @Id
    private Long id;

    private String name;

    public void runAction() {
        log.debug("Some action");
    }

}
