package com.grexdev.nplusone.test.domain.commerce;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.HashSet;
import java.util.Set;

@Getter
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Client {

    @Id
    @EqualsAndHashCode.Include
    private Long id;

    private String firstName;

    private String lastName;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "client", optional = false) // still eager loading, why??? TwoPhaseLoad
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_profile_id")
    private ClientProfile clientProfile;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "client")
    private Set<Order> orders = new HashSet<>();


}
