package com.libraryhub.catalog.service;

import com.libraryhub.catalog.dto.PublisherRequest;
import com.libraryhub.catalog.dto.PublisherResponse;

import java.util.List;

public interface PublisherService {

    PublisherResponse create(PublisherRequest request);

    PublisherResponse update(Long id, PublisherRequest request);

    PublisherResponse getById(Long id);

    List<PublisherResponse> getAll();

    void delete(Long id); // soft delete
}
