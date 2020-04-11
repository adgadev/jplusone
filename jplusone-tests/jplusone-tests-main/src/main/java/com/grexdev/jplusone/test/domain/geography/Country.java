package com.grexdev.jplusone.test.domain.geography;

import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.FetchType.LAZY;

@Entity
@NoArgsConstructor
public class Country {

    @Id
    private Long id;

    private String name;

    @ManyToOne(fetch = LAZY)
    private City capital;

    @OneToMany(mappedBy = "country")
    private Set<Region> regions = new HashSet<>();

}
