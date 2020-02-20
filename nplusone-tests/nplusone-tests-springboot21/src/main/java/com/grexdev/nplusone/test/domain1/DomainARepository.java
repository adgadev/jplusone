package com.grexdev.nplusone.test.domain1;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface DomainARepository extends JpaRepository<DomainA, Long> {

    @Query("SELECT a FROM DomainA a " +
            "LEFT JOIN FETCH a.domainB " +
            "WHERE a.id=:id")
    Optional<DomainA> findByIdAndFetchB(Long id);
}
