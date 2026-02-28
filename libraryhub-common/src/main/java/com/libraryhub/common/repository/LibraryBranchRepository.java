package com.libraryhub.common.repository;

import com.libraryhub.common.entity.LibraryBranch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LibraryBranchRepository extends JpaRepository<LibraryBranch,Long> {


    List<LibraryBranch> findByNameContainingIgnoreCaseAndIsDeletedFalse(String name);

    // Search by city id
    List<LibraryBranch> findByCityCityIdAndIsDeletedFalse(Long cityId);

    // Search by branch name and city id
    List<LibraryBranch> findByNameContainingIgnoreCaseAndCityCityIdAndIsDeletedFalse(String name, Long cityId);
}
