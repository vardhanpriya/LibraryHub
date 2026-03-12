package com.libraryhub.api.controller.payment;

import com.libraryhub.payment.dto.response.CreatePaymentResponse;
import com.libraryhub.payment.dto.response.PaymentTransactionDto;
import com.libraryhub.payment.entity.PaymentTransaction;
import com.libraryhub.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @Value("${razorpay.webhook.secret}")
    private String webhookSecret;

    @PostMapping("/create")
    public CreatePaymentResponse createPayment(@RequestParam Long userId,
                                               @RequestParam BigDecimal amount,
                                               @RequestParam String gateway) throws Exception {
        return paymentService.createPayment(userId, amount, gateway);
    }

    @PostMapping("/verify")
    public String verifyPayment(@RequestBody Map<String,String> payload) throws Exception {

        paymentService.verifyRazorpayPayment(
                payload.get("razorpay_payment_id"),
                payload.get("razorpay_order_id"),
                payload.get("razorpay_signature")
        );

        return "Payment verified successfully";
    }


    @PostMapping("/webhooks/razorpay")
    public String handleRazorpayWebhook(
            @RequestHeader("X-Razorpay-Signature") String signature,
            @RequestBody String payload) {

        paymentService.processRazorpayWebhook(payload, signature, webhookSecret);
        return "OK";
    }

    @PostMapping("/webhooks/stripe")
    public String handleStripeWebhook(@RequestBody String payload,
                                      @RequestHeader("Stripe-Signature") String signature) {

        paymentService.processStripeWebhook(payload, signature);
        return "OK";
    }
}
