package com.libraryhub.api.controller.common;


import com.libraryhub.api.response.ApiResponse;
import com.libraryhub.api.response.ApiResponseBuilder;
import com.libraryhub.common.dto.LibraryBranchReq;
import com.libraryhub.common.dto.LibraryBranchResp;
import com.libraryhub.common.entity.LibraryBranch;
import com.libraryhub.common.service.LibraryBranchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/branches")
@RequiredArgsConstructor
public class LibraryBranchController {

    private final LibraryBranchService service;


    // Create
    @PostMapping
    public ResponseEntity<ApiResponse<LibraryBranchResp>> create(@RequestBody LibraryBranchReq branch) {
        LibraryBranchResp resp = service.createBranch(branch);
        return  ResponseEntity.ok(ApiResponseBuilder.success(resp));
    }

    // Get All
    @GetMapping
    public ResponseEntity<ApiResponse<List<LibraryBranchResp>>> getAll() {
        List<LibraryBranchResp> allBranches = service.getAllBranches();
        return  ResponseEntity.ok(ApiResponseBuilder.success(allBranches));

    }

    // Get By ID
    @GetMapping("/{id}")
    public LibraryBranch getById(@PathVariable Long id) {
        return service.getBranchById(id)
                .orElseThrow(() -> new RuntimeException("Branch not found"));
    }

    // Update
    @PutMapping("/{id}")
    public LibraryBranch update(@PathVariable Long id,
                                @RequestBody LibraryBranch branch) {
        return service.updateBranch(id, branch);
    }

    // Soft Delete
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteBranch(id);
    }

    //  Search by Name
    @GetMapping("/search/by-name")
    public List<LibraryBranch> searchByName(@RequestParam String name) {
        return service.searchByName(name);
    }

    // Search by City
    @GetMapping("/search/by-city")
    public List<LibraryBranch> searchByCity(@RequestParam Long cityId) {
        return service.searchByCity(cityId);
    }

    //  Search by Name and City
    @GetMapping("/search")
    public List<LibraryBranch> searchByNameAndCity(
            @RequestParam String name,
            @RequestParam Long cityId) {
        return service.searchByNameAndCity(name, cityId);
    }
}
