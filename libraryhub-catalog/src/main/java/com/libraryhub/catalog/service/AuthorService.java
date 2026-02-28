package com.libraryhub.catalog.service;


import com.libraryhub.catalog.dto.AuthorRequest;
import com.libraryhub.catalog.dto.AuthorResponse;

import java.util.List;

public interface AuthorService {

    AuthorResponse create(AuthorRequest request);

    AuthorResponse update(Long id, AuthorRequest request);

    AuthorResponse getById(Long id);

    List<AuthorResponse> getAll();

    void delete(Long id); // soft delete
}
