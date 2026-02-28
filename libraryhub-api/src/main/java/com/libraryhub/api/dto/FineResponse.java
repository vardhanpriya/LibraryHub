package com.libraryhub.api.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class FineResponse {
    private Long loanId;
    private BigDecimal amount;
    private LocalDate dueDate;
    private String branchName;
    private String status;
    private BigDecimal paidAmount;
    private LocalDate paidDate;
    private String bookTitle;
}
