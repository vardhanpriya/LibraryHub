package com.libraryhub.circulation.repository;

import com.libraryhub.circulation.entity.Fine;
import com.libraryhub.circulation.entity.LoanPolicy;
import com.libraryhub.common.entity.LibraryBranch;
import com.libraryhub.identity.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LoanPolicyRepository extends JpaRepository<LoanPolicy,Long> {

    Optional<LoanPolicy> findByBranchAndRole(LibraryBranch branch, Role role);

    List<LoanPolicy> findByBranch(LibraryBranch branch);

    List<LoanPolicy> findByRole(Role role);

    List<LoanPolicy> findByStatus(String status);

    Optional<LoanPolicy> findByBranchIsNullAndRole(Role role);

    Optional<LoanPolicy> findByBranchIsNullAndRoleIsNull();


}
