package com.libraryhub.payment.service.impl;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.libraryhub.payment.dto.response.CreatePaymentResponse;
import com.libraryhub.payment.dto.response.PaymentTransactionDto;
import com.libraryhub.payment.entity.Payment;
import com.libraryhub.payment.entity.PaymentTransaction;
import com.libraryhub.payment.enums.PaymentFor;
import com.libraryhub.payment.gateway.PaymentGatewayService;
import com.libraryhub.payment.repository.PaymentRepository;
import com.libraryhub.payment.repository.PaymentTransactionRepository;
import com.libraryhub.payment.service.PaymentService;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
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

    @Value("${stripe.webhook-secret}")
    String stripeWebhookSecret;

    private final ObjectMapper mapper = new ObjectMapper();
    @Override
    public CreatePaymentResponse createPayment(Long userId, BigDecimal amount, String gatewayName) throws Exception {
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
            System.out.println("Inside Stripe webhook");
            Event event = Webhook.constructEvent(payload, signature, stripeWebhookSecret);

            if ("checkout.session.completed".equals(event.getType())) {
                System.out.println("**********checkout.session.completed********");
                Session session = (Session) event.getDataObjectDeserializer()
                        .getObject().orElse(null);
                if (session == null) return;

                PaymentTransaction txn = transactionRepository.findByGatewayOrderId(session.getId()).orElseThrow();
                if ("SUCCESS".equals(txn.getStatus())) {return;}

                txn.setGatewayPaymentId(session.getPaymentIntent());
                txn.setStatus("SUCCESS");
                txn.setGatewayResponse(payload);

                transactionRepository.save(txn);

                updatePaymentSuccess(txn);

            }

        } catch (Exception e) {
            throw new RuntimeException("Stripe webhook verification failed", e);
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
}
