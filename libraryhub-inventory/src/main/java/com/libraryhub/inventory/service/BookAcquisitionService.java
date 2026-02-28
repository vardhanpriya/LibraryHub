package com.libraryhub.inventory.service;

import com.libraryhub.inventory.dto.BookAcquisitionRequest;
import com.libraryhub.inventory.dto.BookAcquisitionResponse;
import org.springframework.data.domain.Page;

public interface BookAcquisitionService {
    BookAcquisitionResponse create(BookAcquisitionRequest request);

    BookAcquisitionResponse update(Long id, BookAcquisitionRequest request);

    BookAcquisitionResponse getById(Long id);

    Page<BookAcquisitionResponse> search(String keyword, int page, int size);

    void delete(Long id);
}
