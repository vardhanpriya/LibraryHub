package com.libraryhub.inventory.repository;

import com.libraryhub.inventory.entity.BookCopy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookCopyRepository extends JpaRepository<BookCopy, Long> {

    List<BookCopy> findByBook_TitleContainingIgnoreCase(String title);

    List<BookCopy> findByBook_Category_NameIgnoreCase(String category);

    List<BookCopy> findByBook_Publisher_NameIgnoreCase(String publisher);

    List<BookCopy> findByBranch_BranchId(Long branchId);
}
