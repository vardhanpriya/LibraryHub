package com.libraryhub.catalog.repository;

import com.libraryhub.catalog.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

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
}
