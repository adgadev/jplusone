package com.grexdev.nplusone.test.domain1;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "table_b")
@Getter
@ToString
@Slf4j
class DomainB {

    @Id
    @GeneratedValue
    private Long id;

    @Setter
    @Access(AccessType.PROPERTY)
    private String name;

    public void action() {
        log.info(getName() + 'a');
    }

}
