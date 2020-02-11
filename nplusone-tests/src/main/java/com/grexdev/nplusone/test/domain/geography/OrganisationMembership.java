package com.grexdev.nplusone.test.domain.geography;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;

import static javax.persistence.FetchType.LAZY;

@Entity
@IdClass(OrganisationRelationshipId.class)
public class OrganisationMembership {

    @Id
    @ManyToOne(fetch = LAZY)
    private Organisation organisation;

    @Id
    @ManyToOne(fetch = LAZY)
    private Country country;

    private boolean active;

}
