package com.libraryhub.circulation.entity;

import com.libraryhub.common.entity.LibraryBranch;
import com.libraryhub.identity.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "FINE")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Fine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fine_id")
    private Long fineId;

    private BigDecimal amount;

    private Boolean paid;

    @Column(name = "paid_date")
    private LocalDate paidDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_id", nullable = false)
    private Loan loan;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
