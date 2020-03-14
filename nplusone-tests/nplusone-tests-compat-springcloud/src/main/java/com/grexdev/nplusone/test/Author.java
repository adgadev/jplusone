package com.grexdev.nplusone.test;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Entity
class Author {

    @Id
    private Long id;

    private String name;

}
