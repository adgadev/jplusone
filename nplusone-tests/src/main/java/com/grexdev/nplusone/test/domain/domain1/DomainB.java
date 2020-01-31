package com.grexdev.nplusone.test.domain.domain1;

import lombok.Getter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "table_b")
@Getter
@ToString
class DomainB {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

}
