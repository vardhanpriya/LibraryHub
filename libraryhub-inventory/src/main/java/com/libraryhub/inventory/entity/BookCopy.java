package com.libraryhub.inventory.entity;

import com.libraryhub.catalog.entity.Book;
import com.libraryhub.common.entity.LibraryBranch;
import com.libraryhub.inventory.dto.BookCopyStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "BOOK_COPY")
@Getter
@Setter
@NoArgsConstructor

public class BookCopy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "copy_id")
    private Long copyId;

    @Column(nullable = false, unique = true)
    private String barcode;

    @Column(name = "shelf_location")
    private String shelfLocation;


    private String status;    // e.g., AVAILABLE, LOANED, DAMAGED , RESERVED
    private String condition; // e.g., NEW, GOOD, POOR

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;  // Reference from Catalog module

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false)
    private LibraryBranch branch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "acquisition_id")
    private BookAcquisition acquisition;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "is_deleted",nullable = false)
    private boolean isDeleted = false;
}
