package com.libraryhub.api.controller.circulation;


import com.libraryhub.circulation.request.HolidayRequest;
import com.libraryhub.circulation.response.HolidayResponse;
import com.libraryhub.circulation.service.HolidayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/holidays")
@RequiredArgsConstructor
public class HolidayController {

    private final HolidayService service;

    @PostMapping
    public ResponseEntity<HolidayResponse> create(@RequestBody HolidayRequest request) {
        return ResponseEntity.ok(service.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HolidayResponse> update(@PathVariable Long id,
                                                  @RequestBody HolidayRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<HolidayResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<HolidayResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/applicable")
    public ResponseEntity<List<HolidayResponse>> getApplicable(
            @RequestParam Long branchId,
            @RequestParam LocalDate date) {
        return ResponseEntity.ok(service.getApplicableHolidays(branchId, date));
    }
}
