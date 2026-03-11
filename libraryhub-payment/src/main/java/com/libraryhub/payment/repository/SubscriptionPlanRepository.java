package com.libraryhub.payment.repository;

import com.libraryhub.payment.entity.SubscriptionPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriptionPlanRepository  extends JpaRepository<SubscriptionPlan,Long> {
    List<SubscriptionPlan> findByStatus(String status);
}
