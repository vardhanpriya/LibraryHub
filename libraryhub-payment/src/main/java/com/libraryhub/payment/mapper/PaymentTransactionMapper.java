package com.libraryhub.payment.mapper;

import com.libraryhub.payment.dto.response.PaymentTransactionDto;
import com.libraryhub.payment.entity.PaymentTransaction;

public class PaymentTransactionMapper {

    public static PaymentTransactionDto toDto(PaymentTransaction transaction) {

        return PaymentTransactionDto.builder()
                .id(transaction.getId())
                .payment(PaymentMapper.toDto(transaction.getPayment()))
                .gateway(transaction.getGateway())
                .gatewayOrderId(transaction.getGatewayOrderId())
                .gatewayPaymentId(transaction.getGatewayPaymentId())
                .gatewaySignature(transaction.getGatewaySignature())
                .status(transaction.getStatus())
                .gatewayResponse(transaction.getGatewayResponse())
                .createdAt(transaction.getCreatedAt())
                .build();
    }
}
