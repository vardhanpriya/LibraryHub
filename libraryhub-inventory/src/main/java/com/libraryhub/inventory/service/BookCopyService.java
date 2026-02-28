package com.libraryhub.inventory.service;

import com.libraryhub.inventory.dto.BookCopyRequest;
import com.libraryhub.inventory.dto.BookCopyResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookCopyService {
    BookCopyResponse create(BookCopyRequest request);

    BookCopyResponse update(Long id, BookCopyRequest request);

    void delete(Long id);

    BookCopyResponse getById(Long id);

    Page<BookCopyResponse> getAll(Pageable pageable);

    Page<BookCopyResponse> getByBook(Long bookId, Pageable pageable);

    Page<BookCopyResponse> getByBranch(Long branchId, Pageable pageable);

    Page<BookCopyResponse> getByStatus(String status, Pageable pageable);
}
