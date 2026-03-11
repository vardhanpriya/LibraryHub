package com.libraryhub.payment.gateway;


import com.libraryhub.payment.entity.Payment;
import com.libraryhub.payment.entity.PaymentTransaction;
import com.libraryhub.payment.repository.PaymentRepository;
import com.libraryhub.payment.repository.PaymentTransactionRepository;
import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service("STRIPE")
public class StripePaymentService implements PaymentGatewayService{

    private final PaymentTransactionRepository paymentTrxnRepo;
    private final PaymentRepository paymentRepository;

    public StripePaymentService(@Value("${stripe.secret-key}") String apiKey,
                                PaymentTransactionRepository transactionRepository, PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
        Stripe.apiKey = apiKey;
        this.paymentTrxnRepo = transactionRepository;
    }


    @Override
    public PaymentTransaction createPayment(Long paymentId, BigDecimal amount) throws Exception {
        Payment payment = paymentRepository.findById(paymentId).get();

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(amount.multiply(BigDecimal.valueOf(100)).longValue()) // in cents
                .setCurrency("inr")
                .build();


        PaymentIntent paymentIntent = PaymentIntent.create(params);



        PaymentTransaction transaction = PaymentTransaction.builder()
                .payment(payment)
                .gateway("STRIPE")
                .gatewayOrderId(paymentIntent.getId())
                .status("CREATED")
                .createdAt(LocalDateTime.now())
                .build();

        return paymentTrxnRepo.save(transaction);
    }
}
