package com.libraryhub.payment.repository;

import com.libraryhub.payment.entity.SubscriptionPlan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionPlanRepository  extends JpaRepository<SubscriptionPlan,Long> {
}
