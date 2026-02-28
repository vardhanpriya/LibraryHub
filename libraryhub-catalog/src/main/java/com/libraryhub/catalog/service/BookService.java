package com.libraryhub.catalog.service;

import com.libraryhub.catalog.dto.BookRequest;
import com.libraryhub.catalog.dto.BookResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BookService {

    BookResponse create(BookRequest request);

    BookResponse update(Long id, BookRequest request);

    BookResponse getById(Long id);

    List<BookResponse> getAll();

    void delete(Long id);

    Page<BookResponse> search(String keyword, int page, int size, String sortBy, String direction);
}
