package com.libraryhub.payment.gateway;

import com.libraryhub.payment.entity.PaymentTransaction;

import java.math.BigDecimal;

public interface PaymentGatewayService {
    PaymentTransaction createPayment(Long paymentId, BigDecimal amount) throws Exception;
}
