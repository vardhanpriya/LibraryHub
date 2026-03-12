package com.libraryhub.payment.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.libraryhub.payment.enums.PaymentFor;
import com.libraryhub.payment.enums.PaymentMethod;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentDto {

    private Long paymentId;
    private Long userId;
    private Long subscriptionId;

    private BigDecimal amount;
    private String currency;

    private PaymentFor paymentFor;
    private Long referenceId;

    private PaymentMethod paymentMethod;

    private String paymentStatus;
    private String transactionId;

    private LocalDateTime paymentDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
