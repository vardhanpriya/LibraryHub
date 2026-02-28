package com.libraryhub.circulation.service.impl;

import com.libraryhub.circulation.entity.LoanPolicy;
import com.libraryhub.circulation.repository.LoanPolicyRepository;
import com.libraryhub.circulation.service.LoanPolicyService;
import com.libraryhub.common.entity.LibraryBranch;
import com.libraryhub.identity.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoanPolicyServiceImpl implements LoanPolicyService {

    private final LoanPolicyRepository repository;

    @Override
    public LoanPolicy createPolicy(LoanPolicy loanPolicy) {
        loanPolicy.setCreatedAt(LocalDateTime.now());
        return repository.save(loanPolicy);
    }

    @Override
    public LoanPolicy updatePolicy(Long policyId, LoanPolicy loanPolicy) {
        return repository.findById(policyId)
                .map(existing -> {
                    existing.setBranch(loanPolicy.getBranch());
                    existing.setRole(loanPolicy.getRole());
                    existing.setMaxBooks(loanPolicy.getMaxBooks());
                    existing.setLoanDays(loanPolicy.getLoanDays());
                    existing.setGraceDays(loanPolicy.getGraceDays());
                    existing.setFinePerDay(loanPolicy.getFinePerDay());
                    existing.setStatus(loanPolicy.getStatus());
                    existing.setUpdatedAt(LocalDateTime.now());
                    return repository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("LoanPolicy not found"));
    }

    @Override
    public void deletePolicy(Long policyId) {
        repository.deleteById(policyId);
    }

    @Override
    public LoanPolicy getPolicyById(Long policyId) {
        return repository.findById(policyId)
                .orElseThrow(() -> new RuntimeException("LoanPolicy not found"));
    }

    @Override
    public List<LoanPolicy> getAllPolicies() {
        return repository.findAll();
    }

    @Override
    public List<LoanPolicy> getPoliciesByBranch(LibraryBranch branch) {
        return repository.findByBranch(branch);
    }

    @Override
    public List<LoanPolicy> getPoliciesByRole(Role role) {
        return repository.findByRole(role);
    }

    @Override
    public Optional<LoanPolicy> getEffectivePolicy(LibraryBranch branch, Role role) {
        //  Branch + Role
        Optional<LoanPolicy> policy = repository.findByBranchAndRole(branch, role);
        if (policy.isPresent()) return policy;

        //  Role-wide default (branch = null)
        policy = repository.findByBranchIsNullAndRole(role);
        if (policy.isPresent()) return policy;

        // Global default (branch = null, role = null)
        return repository.findByBranchIsNullAndRoleIsNull();
    }
}
