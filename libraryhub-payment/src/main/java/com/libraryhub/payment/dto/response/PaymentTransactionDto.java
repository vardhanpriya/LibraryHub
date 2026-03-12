package com.libraryhub.payment.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PaymentTransactionDto {

    private Long id;

    private Long paymentId;

    private PaymentDto payment;

    private String gateway;
    private String gatewayOrderId;
    private String gatewayPaymentId;
    private String gatewaySignature;

    private String status;
    private String gatewayResponse;

    private LocalDateTime createdAt;
    private String stripeClientSecret;
}
