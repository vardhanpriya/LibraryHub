package com.libraryhub.payment.service.impl;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.libraryhub.payment.entity.Payment;
import com.libraryhub.payment.entity.PaymentTransaction;
import com.libraryhub.payment.enums.PaymentFor;
import com.libraryhub.payment.gateway.PaymentGatewayService;
import com.libraryhub.payment.repository.PaymentRepository;
import com.libraryhub.payment.repository.PaymentTransactionRepository;
import com.libraryhub.payment.service.PaymentService;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentTransactionRepository transactionRepository;
    private final Map<String, PaymentGatewayService> gatewayServices;


    private final SubscriptionService subscriptionService;

    @Value("${razorpay.key-secret}")
    String razorpaySecret;

    private final ObjectMapper mapper = new ObjectMapper();
    @Override
    public PaymentTransaction createPayment(Long userId, BigDecimal amount, String gatewayName) throws Exception {
        Payment payment = Payment.builder()
                .userId(userId)
                .amount(amount)
                .currency("INR")
                .paymentFor(PaymentFor.SUBSCRIPTION)
                .paymentStatus("CREATED")
                .createdAt(LocalDateTime.now())
                .build();

        payment = paymentRepository.save(payment);

        // Call appropriate gateway
        PaymentGatewayService gatewayService = gatewayServices.get(gatewayName.toUpperCase());
        if (gatewayService == null) throw new RuntimeException("Unsupported gateway: " + gatewayName);

        return gatewayService.createPayment(payment.getPaymentId(), amount);

    }

    @Override
    public void processRazorpayWebhook(String payload, String signature, String secret) {
        try {

            JsonNode json = mapper.readTree(payload);

            String event = json.get("event").asText();

            if ("payment.captured".equals(event)) {

                JsonNode paymentEntity = json.get("payload").get("payment").get("entity");

                String razorpayPaymentId = paymentEntity.get("id").asText();
                String orderId = paymentEntity.get("order_id").asText();

                PaymentTransaction txn = transactionRepository.findByGatewayOrderId(orderId).orElseThrow();

                txn.setGatewayPaymentId(razorpayPaymentId);
                txn.setStatus("SUCCESS");
                txn.setGatewayResponse(payload);

                transactionRepository.save(txn);

                updatePaymentSuccess(txn);

            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private void updatePaymentSuccess(PaymentTransaction txn) {

        Payment payment =
                paymentRepository.findById(txn.getPayment().getPaymentId()).orElseThrow();

        payment.setPaymentStatus("SUCCESS");
        payment.setPaymentDate(LocalDateTime.now());

        paymentRepository.save(payment);

        subscriptionService.createSubscriptionAfterPayment(payment);
    }

    @Override
    public void processStripeWebhook(String payload, String signature) {
        try {

            JsonNode json = mapper.readTree(payload);

            String event = json.get("type").asText();

            if ("payment_intent.succeeded".equals(event)) {

                JsonNode paymentIntent = json.get("data").get("object");

                String paymentIntentId = paymentIntent.get("id").asText();

                PaymentTransaction txn = transactionRepository.findByGatewayOrderId(paymentIntentId)
                                .orElseThrow();

                txn.setGatewayPaymentId(paymentIntentId);
                txn.setStatus("SUCCESS");
                txn.setGatewayResponse(payload);

                transactionRepository.save(txn);

                updatePaymentSuccess(txn);

            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public void verifyRazorpayPayment(String razorpayPaymentId, String razorpayOrderId, String razorpaySignature) throws Exception {
        JSONObject options = new JSONObject();
        options.put("razorpay_payment_id", razorpayPaymentId);
        options.put("razorpay_order_id", razorpayOrderId);
        options.put("razorpay_signature", razorpaySignature);

        boolean isValid = Utils.verifyPaymentSignature(options, razorpaySecret);
        System.out.println("Payment with orderId: "+razorpayOrderId +" is valid : "+ isValid);

        PaymentTransaction transaction =
                transactionRepository.findByGatewayOrderId(razorpayOrderId)
                        .orElseThrow(() -> new RuntimeException("Transaction not found"));

        Payment payment = transaction.getPayment();

        if (isValid) {
            payment.setPaymentStatus("SUCCESS");
            transaction.setStatus("SUCCESS");
            transaction.setGatewayPaymentId(razorpayPaymentId);

        } else {
            payment.setPaymentStatus("FAILED");
            transaction.setStatus("FAILED");

        }
        paymentRepository.save(payment);
        transactionRepository.save(transaction);
    }



    @Transactional
    public void handlePaymentSuccess(PaymentTransaction transaction, String gatewayPaymentId, String signature, String responseJson) {
        transaction.setGatewayPaymentId(gatewayPaymentId);
        transaction.setGatewaySignature(signature);
        transaction.setStatus("SUCCESS");
        transaction.setGatewayResponse(responseJson);
        transactionRepository.save(transaction);

        Payment payment = paymentRepository.findById(transaction.getPayment().getPaymentId()).orElseThrow();
        payment.setPaymentStatus("SUCCESS");
        payment.setPaymentDate(LocalDateTime.now());
        paymentRepository.save(payment);

        // TODO: Create UserSubscription after successful payment
    }
}
