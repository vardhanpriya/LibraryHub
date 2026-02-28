package com.libraryhub.inventory.repository;

import com.libraryhub.inventory.entity.BookAcquisition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@Repository
public interface BookAcquisitionRepository extends JpaRepository<BookAcquisition, Long> {

    List<BookAcquisition> findByBook_TitleContainingIgnoreCase(String title);

    List<BookAcquisition> findByBook_Category_NameIgnoreCase(String category);

    List<BookAcquisition> findByBook_Publisher_NameIgnoreCase(String publisher);

    @Query("""
        SELECT ba FROM BookAcquisition ba
        LEFT JOIN ba.book b
        LEFT JOIN ba.branch br
        WHERE 
            (:keyword IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')))
            OR (:keyword IS NULL OR LOWER(br.name) LIKE LOWER(CONCAT('%', :keyword, '%')))
            OR (:keyword IS NULL OR LOWER(ba.vendor) LIKE LOWER(CONCAT('%', :keyword, '%')))
    """)
    Page<BookAcquisition> search(String keyword, Pageable pageable);
}
