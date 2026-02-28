package com.libraryhub.catalog.repository;

import com.libraryhub.catalog.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    // Search by exact category
    List<Book> findByCategory_NameIgnoreCase(String categoryName);

    // Search by exact publisher
    List<Book> findByPublisher_NameIgnoreCase(String publisherName);

    // Search by title containing keyword
    List<Book> findByTitleContainingIgnoreCase(String title);

    // Combine filters
    List<Book> findByTitleContainingIgnoreCaseAndCategory_NameIgnoreCaseAndPublisher_NameIgnoreCase(
            String title, String categoryName, String publisherName);

    Optional<Book> findByIsbnAndIsDeletedFalse(String isbn);

    List<Book> findAllByIsDeletedFalse();

    @Query("""
        SELECT DISTINCT b FROM Book b
        LEFT JOIN b.authors a
        LEFT JOIN b.category c
        WHERE b.isDeleted = false
        AND (
            (:keyword IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')))
            OR (:keyword IS NULL OR LOWER(a.name) LIKE LOWER(CONCAT('%', :keyword, '%')))
            OR (:keyword IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%')))
        )
    """)
    Page<Book> searchBooks(String keyword, Pageable pageable);
}
