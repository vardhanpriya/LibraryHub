package com.libraryhub.payment.service;

import com.libraryhub.payment.dto.response.CreatePaymentResponse;
import com.libraryhub.payment.dto.response.PaymentTransactionDto;
import com.libraryhub.payment.entity.PaymentTransaction;
import com.razorpay.RazorpayException;

import java.math.BigDecimal;

public interface PaymentService {

    CreatePaymentResponse createPayment(Long userId, BigDecimal amount, String gatewayName) throws Exception;
     void processRazorpayWebhook(String payload, String signature, String secret);
       void processStripeWebhook(String payload, String signature);

    void verifyRazorpayPayment(String razorpayPaymentId, String razorpayOrderId, String razorpaySignature) throws Exception;
}
