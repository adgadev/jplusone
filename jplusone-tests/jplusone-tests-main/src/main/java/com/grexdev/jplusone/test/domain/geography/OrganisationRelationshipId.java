package com.grexdev.jplusone.test.domain.geography;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@EqualsAndHashCode
public class OrganisationRelationshipId implements Serializable {

    private Long organisation;

    private Long country;

}
