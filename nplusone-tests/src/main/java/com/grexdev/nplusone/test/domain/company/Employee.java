package com.grexdev.nplusone.test.domain.company;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Employee {

    @Id
    private long id;

    private String name;

}