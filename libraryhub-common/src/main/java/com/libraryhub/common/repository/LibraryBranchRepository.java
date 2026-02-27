package com.libraryhub.common.repository;

import com.libraryhub.common.entity.LibraryBranch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibraryBranchRepository extends JpaRepository<LibraryBranch,Long> {
}
