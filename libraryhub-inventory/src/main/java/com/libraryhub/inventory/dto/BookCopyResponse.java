package com.libraryhub.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookCopyResponse {

    private Long copyId;
    private String barcode;
    private String shelfLocation;
    private String status;
    private String condition;

    private Long bookId;
    private String bookTitle;

    private Long branchId;
    private String branchName;
}
