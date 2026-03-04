package com.libraryhub.circulation.request;


import com.libraryhub.circulation.entity.Loan;
import com.libraryhub.identity.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateFineRequest {
    private BigDecimal amount;
    private String status;
    private LocalDate paidDate;
    private BigDecimal paidAmount = BigDecimal.ZERO;
    private Long waivedBy;
    private Long loanId;
}
