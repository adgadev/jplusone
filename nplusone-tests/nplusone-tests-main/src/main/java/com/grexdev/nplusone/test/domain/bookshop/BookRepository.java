package com.grexdev.nplusone.test.domain.bookshop;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface BookRepository extends JpaRepository<Book, Long> {

    @Query("SELECT b FROM Book b " +
            "LEFT JOIN FETCH b.author " +
            "WHERE b.id=:id")
    Optional<Book> findByIdAndFetchAuthor(@Param("id") Long id);
}
