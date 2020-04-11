package com.grexdev.jplusone.test.domain.geography;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.FetchType.LAZY;

@Entity
public class Organisation {

    @Id
    private Long id;

    private String name;

    @OneToMany
    private Set<OrganisationMembership> members = new HashSet<>();

    @ManyToOne(fetch = LAZY)
    private City headOffice;

}
