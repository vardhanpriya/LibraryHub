package com.libraryhub.circulation.utility;


import com.libraryhub.circulation.entity.LoanPolicy;
import com.libraryhub.circulation.repository.LoanPolicyRepository;
import com.libraryhub.common.entity.LibraryBranch;
import com.libraryhub.identity.entity.Role;
import com.libraryhub.identity.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class ResolveLoanPolicyService {

    private final LoanPolicyRepository loanPolicyRepository;

    private static final List<String> ROLE_PRIORITY =
            List.of("ADMIN", "LIBRARIAN", "MEMBER");

    public LoanPolicy resolveLoanPolicy(User user, LibraryBranch branch) {

        Set<Role> userRoles = user.getRoles();

        // Sort roles according to priority
        List<Role> sortedRoles = userRoles.stream()
                .sorted(Comparator.comparingInt(r -> ROLE_PRIORITY.indexOf(r.getName()))).toList();

        for (Role role : sortedRoles) {

            //  Branch + Role
            Optional<LoanPolicy> policy =
                    loanPolicyRepository.findFirstByBranchAndRoleAndStatus(
                            branch, role, "ACTIVE");

            if (policy.isPresent()) return policy.get();

            //  Role only
            policy = loanPolicyRepository
                    .findFirstByRoleAndBranchIsNullAndStatus(role, "ACTIVE");

            if (policy.isPresent()) return policy.get();
        }

        // Branch specific
        Optional<LoanPolicy> branchPolicy =
                loanPolicyRepository.findFirstByBranchAndRoleIsNullAndStatus(
                        branch, "ACTIVE");

        if (branchPolicy.isPresent()) return branchPolicy.get();

        // Global
        return loanPolicyRepository
                .findFirstByBranchIsNullAndRoleIsNullAndStatus("ACTIVE")
                .orElseThrow(() -> new RuntimeException("No loan policy configured"));
    }
}
