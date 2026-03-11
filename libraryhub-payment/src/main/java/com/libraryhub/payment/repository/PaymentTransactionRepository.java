package com.libraryhub.payment.repository;

import com.libraryhub.payment.entity.PaymentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction,Long> {


    Optional<PaymentTransaction> findByGatewayOrderId(String gatewayOrderId);
}
