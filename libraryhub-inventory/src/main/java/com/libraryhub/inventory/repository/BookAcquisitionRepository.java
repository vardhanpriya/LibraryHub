package com.libraryhub.inventory.repository;

import com.libraryhub.inventory.entity.BookAcquisition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookAcquisitionRepository extends JpaRepository<BookAcquisition, Long> {

    List<BookAcquisition> findByBook_TitleContainingIgnoreCase(String title);

    List<BookAcquisition> findByBook_Category_NameIgnoreCase(String category);

    List<BookAcquisition> findByBook_Publisher_NameIgnoreCase(String publisher);
}
