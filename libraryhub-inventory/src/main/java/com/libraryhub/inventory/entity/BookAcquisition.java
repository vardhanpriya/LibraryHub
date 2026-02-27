package com.libraryhub.inventory.entity;

import com.libraryhub.catalog.entity.Book;
import com.libraryhub.common.entity.LibraryBranch;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Table(name = "BOOK_ACQUISITION")
@Getter @Setter
@NoArgsConstructor
public class BookAcquisition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "acquisition_id")
    private Long acquisitionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;  // Reference from Catalog module

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false)
    private LibraryBranch branch;

    private String vendor;

    private BigDecimal cost;

    private int quantity;


    @Column(name = "purchase_date")
    private LocalDate purchaseDate;

    private String status; // e.g., RECEIVED, PENDING

    @Column(name = "received_by")
    private Long receivedBy; // Optional: can map to User entity if needed

    @Column(columnDefinition = "TEXT")
    private String remarks;

    @Column(name = "created_at",nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
