package com.grexdev.nplusone.test.domain.geography;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import static javax.persistence.FetchType.LAZY;

@Entity
public class City {

    @Id
    private Long id;

    private String name;

    @ManyToOne(fetch = LAZY)
    private Region region;

    @Embedded
    private GeoCoordinates center;

}
