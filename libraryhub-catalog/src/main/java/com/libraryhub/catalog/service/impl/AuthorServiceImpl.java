package com.libraryhub.catalog.service.impl;

import com.libraryhub.catalog.dto.AuthorRequest;
import com.libraryhub.catalog.dto.AuthorResponse;
import com.libraryhub.catalog.entity.Author;
import com.libraryhub.catalog.repository.AuthorRepository;
import com.libraryhub.catalog.service.AuthorService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository repository;

    @Override
    public AuthorResponse create(AuthorRequest request) {

        repository.findByNameIgnoreCaseAndIsDeletedFalse(request.getName())
                .ifPresent(a -> {
                    throw new RuntimeException("Author already exists");
                });

        Author author = new Author();
        author.setName(request.getName());
        author.setBio(request.getBio());
        author.setCreatedAt(LocalDateTime.now());
        author.setUpdatedAt(LocalDateTime.now());
        author.setDeleted(false);

        author = repository.save(author);

        return mapToResponse(author);
    }

    @Override
    public AuthorResponse update(Long id, AuthorRequest request) {

        Author author = repository.findById(id)
                .filter(a -> !a.isDeleted())
                .orElseThrow(() -> new RuntimeException("Author not found"));

        author.setName(request.getName());
        author.setBio(request.getBio());
        author.setUpdatedAt(LocalDateTime.now());

        return mapToResponse(repository.save(author));
    }

    @Override
    @Transactional
    public AuthorResponse getById(Long id) {
        Author author = repository.findById(id)
                .filter(a -> !a.isDeleted())
                .orElseThrow(() -> new RuntimeException("Author not found"));

        return mapToResponse(author);
    }

    @Override
    @Transactional
    public List<AuthorResponse> getAll() {
        return repository.findAllByIsDeletedFalse()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public void delete(Long id) {
        Author author = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Author not found"));

        author.setDeleted(true);
        author.setUpdatedAt(LocalDateTime.now());

        repository.save(author);
    }

    private AuthorResponse mapToResponse(Author author) {
        return AuthorResponse.builder()
                .authorId(author.getAuthorId())
                .name(author.getName())
                .bio(author.getBio())
                .createdAt(author.getCreatedAt())
                .updatedAt(author.getUpdatedAt())
                .build();
    }
}