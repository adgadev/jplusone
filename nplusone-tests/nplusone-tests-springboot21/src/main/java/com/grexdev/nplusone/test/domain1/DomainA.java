package com.grexdev.nplusone.test.domain1;

import lombok.Getter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Getter
@Table(name = "table_a")
@ToString(exclude = "domainB")
class DomainA {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @JoinColumn(name = "domain_b_id")
    @OneToOne(fetch = FetchType.LAZY)
    private DomainB domainB;

}
