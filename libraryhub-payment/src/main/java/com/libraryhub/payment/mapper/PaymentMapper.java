package com.libraryhub.payment.mapper;

import com.libraryhub.payment.dto.response.PaymentDto;
import com.libraryhub.payment.entity.Payment;

public class PaymentMapper {
    public static PaymentDto toDto(Payment payment) {

        if (payment == null) {
            return null;
        }

        return PaymentDto.builder()
                .paymentId(payment.getPaymentId())
                .userId(payment.getUserId())
                .subscriptionId(payment.getSubscriptionId())
                .amount(payment.getAmount())
                .currency(payment.getCurrency())
                .paymentFor(payment.getPaymentFor())
                .referenceId(payment.getReferenceId())
                .paymentMethod(payment.getPaymentMethod())
                .paymentStatus(payment.getPaymentStatus())
                .transactionId(payment.getTransactionId())
                .paymentDate(payment.getPaymentDate())
                .createdAt(payment.getCreatedAt())
                .updatedAt(payment.getUpdatedAt())
                .build();
    }
}
