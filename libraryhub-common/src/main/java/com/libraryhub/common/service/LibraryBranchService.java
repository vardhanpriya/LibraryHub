package com.libraryhub.common.service;

import com.libraryhub.common.dto.LibraryBranchReq;
import com.libraryhub.common.dto.LibraryBranchResp;
import com.libraryhub.common.entity.LibraryBranch;

import java.util.List;
import java.util.Optional;

public interface LibraryBranchService {
    LibraryBranchResp createBranch(LibraryBranchReq branch);

    // Read
    List<LibraryBranchResp> getAllBranches();

    LibraryBranchResp getBranchById(Long id);

    // Update
    LibraryBranch updateBranch(Long id, LibraryBranch updatedBranch);

    // Delete (Soft Delete)
    void deleteBranch(Long id);

    // Search Operations
    List<LibraryBranch> searchByName(String name);

    List<LibraryBranch> searchByCity(Long cityId);

    List<LibraryBranch> searchByNameAndCity(String name, Long cityId);

}
