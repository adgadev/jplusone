package com.grexdev.jplusone.test.domain.commerce;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

@Getter
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Manufacturer {

    @Id
    private Long id;

    @EqualsAndHashCode.Include
    private String name;

    @OneToMany(mappedBy = "manufacturer", fetch = FetchType.LAZY)
    private Set<Product> products = new HashSet<>();

}
