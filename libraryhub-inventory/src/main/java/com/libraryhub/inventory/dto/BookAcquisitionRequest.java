package com.libraryhub.inventory.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class BookAcquisitionRequest {

    private Long bookId;
    private Long branchId;
    private String vendor;
    private BigDecimal cost;
    private int quantity;
    private LocalDate purchaseDate;
    private String status;
    private Long receivedBy;
    private String remarks;
}
