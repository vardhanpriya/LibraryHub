package com.libraryhub.catalog.service.impl;

import com.libraryhub.catalog.dto.PublisherRequest;
import com.libraryhub.catalog.dto.PublisherResponse;
import com.libraryhub.catalog.entity.Publisher;
import com.libraryhub.catalog.repository.PublisherRepository;
import com.libraryhub.catalog.service.PublisherService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PublisherServiceImpl implements PublisherService {

    private final PublisherRepository repository;

    @Override
    public PublisherResponse create(PublisherRequest request) {

        repository.findByNameIgnoreCaseAndIsDeletedFalse(request.getName())
                .ifPresent(p -> {
                    throw new RuntimeException("Publisher already exists");
                });

        Publisher publisher = new Publisher();
        publisher.setName(request.getName());
        publisher.setAddress(request.getAddress());
        publisher.setCreatedAt(LocalDateTime.now());
        publisher.setUpdatedAt(LocalDateTime.now());
        publisher.setDeleted(false);

        publisher = repository.save(publisher);

        return mapToResponse(publisher);
    }

    @Override
    public PublisherResponse update(Long id, PublisherRequest request) {

        Publisher publisher = repository.findById(id)
                .filter(p -> !p.isDeleted())
                .orElseThrow(() -> new RuntimeException("Publisher not found"));

        publisher.setName(request.getName());
        publisher.setAddress(request.getAddress());
        publisher.setUpdatedAt(LocalDateTime.now());

        return mapToResponse(repository.save(publisher));
    }

    @Override
    @Transactional
    public PublisherResponse getById(Long id) {
        Publisher publisher = repository.findById(id)
                .filter(p -> !p.isDeleted())
                .orElseThrow(() -> new RuntimeException("Publisher not found"));

        return mapToResponse(publisher);
    }

    @Override
    @Transactional
    public List<PublisherResponse> getAll() {
        return repository.findAllByIsDeletedFalse()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public void delete(Long id) {
        Publisher publisher = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Publisher not found"));

        publisher.setDeleted(true);
        publisher.setUpdatedAt(LocalDateTime.now());

        repository.save(publisher);
    }

    private PublisherResponse mapToResponse(Publisher publisher) {
        return PublisherResponse.builder()
                .publisherId(publisher.getPublisherId())
                .name(publisher.getName())
                .address(publisher.getAddress())
                .createdAt(publisher.getCreatedAt())
                .updatedAt(publisher.getUpdatedAt())
                .build();
    }
}
