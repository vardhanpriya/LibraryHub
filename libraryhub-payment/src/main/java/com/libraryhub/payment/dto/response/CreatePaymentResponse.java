package com.libraryhub.payment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreatePaymentResponse {

    private String gateway;

    private Long paymentId;
    private Long transactionId;

    private BigDecimal amount;
    private String currency;

    // Razorpay
    private String razorpayOrderId;
    private String razorpayKey;

    // Stripe
    private String stripeClientSecret;
    private String stripeCheckoutUrl;
}

/*
{
  "gateway": "RAZORPAY",
  "amount": 49900,
  "currency": "INR",
  "razorpayOrderId": "order_NY8h9aJkL",
  "razorpayKey": "rzp_test_xxxxx"
}

{
  "gateway": "STRIPE",
  "clientSecret": "pi_3Nsdfkjsdf_secret_xxxx"
}

{
  "paymentId": 101,
  "transactionId": 22,
  "gateway": "RAZORPAY",
  "amount": 499,
  "currency": "INR",
  "razorpayOrderId": "order_NY8h9aJkL",
  "razorpayKey": "rzp_test_xxx",
  "status": "CREATED"
}


Example Service Mapping (Razorpay)
return CreatePaymentResponse.builder()
        .paymentId(payment.getPaymentId())
        .transactionId(savedTransaction.getId())
        .gateway("RAZORPAY")
        .currency("INR")
        .amount(amount)
        .razorpayOrderId(order.get("id"))
        .razorpayKey(razorpayKey)
        .status("CREATED")
        .build();
         Example Service Mapping (Stripe)
return CreatePaymentResponse.builder()
        .paymentId(payment.getPaymentId())
        .transactionId(savedTransaction.getId())
        .gateway("STRIPE")
        .currency("INR")
        .amount(amount)
        .stripeClientSecret(paymentIntent.getClientSecret())
        .status("CREATED")
        .build();



 {
  "paymentId": 101,
  "transactionId": 22,
  "gateway": "RAZORPAY",
  "currency": "INR",
  "amount": 499,

  "razorpayOrderId": "order_NY8h9aJkL",
  "razorpayKey": "rzp_test_9asdjkl23",

  "stripeClientSecret": null,

  "status": "CREATED"
}

{
  "paymentId": 101,
  "transactionId": 23,
  "gateway": "STRIPE",
  "currency": "INR",
  "amount": 499,

  "razorpayOrderId": null,
  "razorpayKey": null,

  "stripeClientSecret": "pi_3Nsdasdsd_secret_abc123",

  "status": "CREATED"
}
Done! The Stripe CLI is configured for LibraryHub sandbox with account id acct_1T97w4Gv66V5ipsE
 */
