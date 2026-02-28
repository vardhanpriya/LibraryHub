package com.libraryhub.circulation.service;

import com.libraryhub.circulation.entity.LoanPolicy;
import com.libraryhub.common.entity.LibraryBranch;
import com.libraryhub.identity.entity.Role;

import java.util.List;
import java.util.Optional;

public interface LoanPolicyService {

    LoanPolicy createPolicy(LoanPolicy loanPolicy);

    LoanPolicy updatePolicy(Long policyId, LoanPolicy loanPolicy);

    void deletePolicy(Long policyId);

    LoanPolicy getPolicyById(Long policyId);

    List<LoanPolicy> getAllPolicies();

    List<LoanPolicy> getPoliciesByBranch(LibraryBranch branch);

    List<LoanPolicy> getPoliciesByRole(Role role);

    Optional<LoanPolicy> getEffectivePolicy(LibraryBranch branch, Role role);
}