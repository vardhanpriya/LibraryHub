package com.libraryhub.api.controller.inventory;

import com.libraryhub.inventory.dto.BookAcquisitionRequest;
import com.libraryhub.inventory.dto.BookAcquisitionResponse;
import com.libraryhub.inventory.service.BookAcquisitionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/acquisitions")
@RequiredArgsConstructor
public class BookAcquisitionController {

    private final BookAcquisitionService service;

    @PostMapping
    public BookAcquisitionResponse create(@RequestBody BookAcquisitionRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public BookAcquisitionResponse update(@PathVariable Long id,
                                          @RequestBody BookAcquisitionRequest request) {
        return service.update(id, request);
    }

    @GetMapping("/{id}")
    public BookAcquisitionResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping
    public Page<BookAcquisitionResponse> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return service.search(keyword, page, size);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
