package com.libraryhub.inventory.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class BookAcquisitionResponse {

    private Long acquisitionId;

    private Long bookId;
    private String bookTitle;

    private Long branchId;
    private String branchName;

    private String vendor;
    private BigDecimal cost;
    private int quantity;
    private LocalDate purchaseDate;
    private String status;
    private Long receivedBy;
    private String remarks;

    private LocalDateTime createdAt;
}