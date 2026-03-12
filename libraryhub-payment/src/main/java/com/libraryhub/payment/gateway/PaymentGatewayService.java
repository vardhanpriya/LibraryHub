package com.libraryhub.payment.gateway;

import com.libraryhub.payment.dto.response.CreatePaymentResponse;
import com.libraryhub.payment.dto.response.PaymentTransactionDto;
import com.libraryhub.payment.entity.PaymentTransaction;

import java.math.BigDecimal;

public interface PaymentGatewayService {
    CreatePaymentResponse createPayment(Long paymentId, BigDecimal amount) throws Exception;
}
