package com.libraryhub.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookCopyRequest {

    private String barcode;
    private String shelfLocation;
    private String status;
    private String condition;

    private Long bookId;
    private Long branchId;
    private Long acquisitionId;
}

