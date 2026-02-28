package com.libraryhub.api.controller.circulation;

import com.libraryhub.circulation.entity.LoanPolicy;
import com.libraryhub.circulation.service.LoanPolicyService;
import com.libraryhub.common.entity.LibraryBranch;
import com.libraryhub.identity.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loan-policies")
@RequiredArgsConstructor
public class LoanPolicyController {

    private final LoanPolicyService service;

    @PostMapping
    public ResponseEntity<LoanPolicy> create(@RequestBody LoanPolicy policy) {
        return ResponseEntity.ok(service.createPolicy(policy));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LoanPolicy> update(@PathVariable Long id,
                                             @RequestBody LoanPolicy policy) {
        return ResponseEntity.ok(service.updatePolicy(id, policy));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deletePolicy(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoanPolicy> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getPolicyById(id));
    }

    @GetMapping
    public ResponseEntity<List<LoanPolicy>> getAll() {
        return ResponseEntity.ok(service.getAllPolicies());
    }

    @GetMapping("/branch/{branchId}")
    public ResponseEntity<List<LoanPolicy>> getByBranch(@PathVariable Long branchId) {
        LibraryBranch branch = new LibraryBranch();
        branch.setBranchId(branchId);
        return ResponseEntity.ok(service.getPoliciesByBranch(branch));
    }

    @GetMapping("/role/{roleId}")
    public ResponseEntity<List<LoanPolicy>> getByRole(@PathVariable Long roleId) {
        Role role = new Role();
        role.setRoleId(roleId);
        return ResponseEntity.ok(service.getPoliciesByRole(role));
    }
}
