package com.libraryhub.api.controller.catalog;

import com.libraryhub.catalog.dto.AuthorRequest;
import com.libraryhub.catalog.dto.AuthorResponse;
import com.libraryhub.catalog.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/authors")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService service;

    @PostMapping
    public AuthorResponse create(@RequestBody AuthorRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public AuthorResponse update(@PathVariable Long id,
                                 @RequestBody AuthorRequest request) {
        return service.update(id, request);
    }

    @GetMapping("/{id}")
    public AuthorResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping
    public List<AuthorResponse> getAll() {
        return service.getAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
