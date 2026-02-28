package com.libraryhub.catalog.service;

import com.libraryhub.catalog.dto.CategoryRequest;
import com.libraryhub.catalog.dto.CategoryResponse;

import java.util.List;

public interface CategoryService {


    CategoryResponse create(CategoryRequest request);

    CategoryResponse update(Long id, CategoryRequest request);

    CategoryResponse getById(Long id);

    List<CategoryResponse> getAll();

    void delete(Long id);   // soft delete
}
