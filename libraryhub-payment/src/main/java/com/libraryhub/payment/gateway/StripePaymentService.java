package com.libraryhub.payment.gateway;


import com.libraryhub.payment.dto.response.CreatePaymentResponse;
import com.libraryhub.payment.dto.response.PaymentTransactionDto;
import com.libraryhub.payment.entity.Payment;
import com.libraryhub.payment.entity.PaymentTransaction;
import com.libraryhub.payment.mapper.PaymentTransactionMapper;
import com.libraryhub.payment.repository.PaymentRepository;
import com.libraryhub.payment.repository.PaymentTransactionRepository;
import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service("STRIPE")
public class StripePaymentService implements PaymentGatewayService{

    private final PaymentTransactionRepository paymentTrxnRepo;
    private final PaymentRepository paymentRepository;
    @Value("${stripe.secret-key}")
    private String stripeSecretKey;

    public StripePaymentService(@Value("${stripe.secret-key}") String apiKey,
                                PaymentTransactionRepository transactionRepository, PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
        Stripe.apiKey = apiKey;
        this.paymentTrxnRepo = transactionRepository;
    }


    @Override
    public CreatePaymentResponse createPayment(Long paymentId, BigDecimal amount) throws Exception {
        Payment payment = paymentRepository.findById(paymentId).get();

        Stripe.apiKey = stripeSecretKey;
        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:5173/payment-success?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl("http://localhost:5173/payment-cancel")
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency("INR")
                                                .setUnitAmount(amount.multiply(new BigDecimal(100)).longValue())
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName("LibraryHub Subscription")
                                                                .build()
                                                )
                                                .build()
                                )
                                .build()
                )
                .build();

        Session session = Session.create(params);

        PaymentTransaction txn = PaymentTransaction.builder()
                .payment(payment)
                .gateway("STRIPE")
                .gatewayOrderId(session.getId())
                .status("CREATED")
                .createdAt(LocalDateTime.now())
                .build();

        paymentTrxnRepo.save(txn);

        CreatePaymentResponse response = new CreatePaymentResponse();
        response.setPaymentId(payment.getPaymentId());
        response.setTransactionId(txn.getId());
        response.setGateway("STRIPE");
        response.setStripeCheckoutUrl(session.getUrl()); // Popup URL
        response.setAmount(amount);
        response.setCurrency("INR");
        return response;



//        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
//                .setAmount(amount.multiply(BigDecimal.valueOf(100)).longValue()) // in cents
//                .setCurrency("inr")
//                .build();
//        PaymentIntent paymentIntent = PaymentIntent.create(params);
//        PaymentTransaction transaction = PaymentTransaction.builder()
//                .payment(payment)
//                .gateway("STRIPE")
//                .gatewayOrderId(paymentIntent.getId())
//                .status("CREATED")
//                .createdAt(LocalDateTime.now())
//                .build();
//        PaymentTransaction saved =  paymentTrxnRepo.save(transaction);
//        CreatePaymentResponse response = new CreatePaymentResponse();
//          response.setPaymentId(paymentId);
//             response.setTransactionId(saved.getId());
//             response.setGateway("STRIPE");
//             response.setStripeClientSecret(paymentIntent.getClientSecret());
//             response.setAmount(amount);
//             response.setCurrency("inr");
//         return response;
    }
}
