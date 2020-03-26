package com.grexdev.nplusone.test.domain.commerce;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Getter
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ClientProfile {

    @Id
    @EqualsAndHashCode.Include
    private Long id;

    private String photoLink;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "clientProfile")
    private Client client;

}
