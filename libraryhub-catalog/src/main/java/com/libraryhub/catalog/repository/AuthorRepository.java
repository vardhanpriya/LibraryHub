package com.libraryhub.catalog.repository;

import com.libraryhub.catalog.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    Optional<Author> findByNameIgnoreCase(String name);

    Optional<Author> findByNameIgnoreCaseAndIsDeletedFalse(String name);

    List<Author> findAllByIsDeletedFalse();
}
