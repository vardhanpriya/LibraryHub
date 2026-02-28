package com.libraryhub.api.controller.catalog;

import com.libraryhub.catalog.dto.PublisherRequest;
import com.libraryhub.catalog.dto.PublisherResponse;
import com.libraryhub.catalog.service.PublisherService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/publishers")
@RequiredArgsConstructor
public class PublisherController {

    private final PublisherService service;

    @PostMapping
    public PublisherResponse create(@RequestBody PublisherRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public PublisherResponse update(@PathVariable Long id,
                                    @RequestBody PublisherRequest request) {
        return service.update(id, request);
    }

    @GetMapping("/{id}")
    public PublisherResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping
    public List<PublisherResponse> getAll() {
        return service.getAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}