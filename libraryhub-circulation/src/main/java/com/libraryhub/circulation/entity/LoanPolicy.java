package com.libraryhub.circulation.entity;

import com.libraryhub.common.entity.LibraryBranch;
import com.libraryhub.identity.entity.Role;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "LOAN_POLICY")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanPolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "policy_id")
    private Long policyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false)
    private LibraryBranch branch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Column(name = "max_books", nullable = false)
    private Integer maxBooks;

    @Column(name = "loan_days", nullable = false)
    private Integer loanDays;

    @Column(name = "grace_days", nullable = false)
    private Integer graceDays;

    @Column(name = "fine_per_day", nullable = false, precision = 10, scale = 2)
    private BigDecimal finePerDay;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
