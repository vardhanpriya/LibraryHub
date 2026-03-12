package com.libraryhub.payment.gateway;


import com.libraryhub.payment.dto.response.CreatePaymentResponse;
import com.libraryhub.payment.dto.response.PaymentTransactionDto;
import com.libraryhub.payment.entity.Payment;
import com.libraryhub.payment.entity.PaymentTransaction;
import com.libraryhub.payment.mapper.PaymentTransactionMapper;
import com.libraryhub.payment.repository.PaymentRepository;
import com.libraryhub.payment.repository.PaymentTransactionRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service("RAZORPAY")
public class RazorpayPaymentService implements  PaymentGatewayService{

    private final RazorpayClient client;
    private final PaymentTransactionRepository transactionRepository;
    private final PaymentRepository paymentRepository;
    private final String keyId;

    public RazorpayPaymentService( @Value("${razorpay.key-id}") String keyId,
                                   @Value("${razorpay.key-secret}") String keySecret,
                                  PaymentTransactionRepository transactionRepository, PaymentRepository paymentRepository) throws RazorpayException {
        this.keyId = keyId;
        this.paymentRepository = paymentRepository;
        this.client = new RazorpayClient(keyId, keySecret);
        this.transactionRepository = transactionRepository;
    }
    @Override
    public CreatePaymentResponse createPayment(Long paymentId, BigDecimal amount) throws Exception {


        JSONObject options = new JSONObject();
        options.put("amount", amount.multiply(BigDecimal.valueOf(100))); // in paise
        options.put("currency", "INR");
        options.put("receipt", paymentId.toString());
        options.put("payment_capture", 1);

        Order order = client.orders.create(options);

        Payment payment = paymentRepository.findById(paymentId).get();

        PaymentTransaction transaction = PaymentTransaction.builder()
                .payment(payment)
                .gateway("RAZORPAY")
                .gatewayOrderId(order.get("id"))
                .status("CREATED")
                .createdAt(LocalDateTime.now())
                .build();

        PaymentTransaction saved = transactionRepository.save(transaction);
        CreatePaymentResponse response = new CreatePaymentResponse();
            response.setPaymentId(paymentId);
            response.setTransactionId(saved.getId());
            response.setCurrency("INR");
            response.setGateway("RAZORPAY");
            response.setAmount(amount);
            response.setRazorpayKey(keyId);
            response.setRazorpayOrderId(order.get("id"));
            return  response;
    }
}
