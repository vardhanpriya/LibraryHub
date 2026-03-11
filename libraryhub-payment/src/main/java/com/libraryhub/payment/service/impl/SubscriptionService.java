package com.libraryhub.payment.service.impl;


import com.libraryhub.payment.entity.Payment;
import com.libraryhub.payment.entity.SubscriptionPlan;
import com.libraryhub.payment.entity.UserSubscription;
import com.libraryhub.payment.repository.SubscriptionPlanRepository;
import com.libraryhub.payment.repository.UserSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SubscriptionService {
    private final SubscriptionPlanRepository planRepository;
    private final UserSubscriptionRepository subscriptionRepository;

    public void createSubscriptionAfterPayment(Payment payment) {

        if (!"SUBSCRIPTION".equals(payment.getPaymentFor())) return;

        SubscriptionPlan plan = planRepository.findById(payment.getReferenceId()).orElseThrow();

        LocalDate start = LocalDate.now();
        LocalDate end = start.plusDays(plan.getDurationDays());

        UserSubscription subscription = UserSubscription.builder()
                .userId(payment.getUserId())
                .planId(plan.getPlanId())
                .startDate(start)
                .endDate(end)
                .status("ACTIVE")
                .autoRenew(false)
                .createdAt(LocalDateTime.now())
                .build();

        subscriptionRepository.save(subscription);

        payment.setSubscriptionId(subscription.getSubscriptionId());
    }

}
