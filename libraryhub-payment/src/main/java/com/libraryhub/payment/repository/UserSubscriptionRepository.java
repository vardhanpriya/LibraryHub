package com.libraryhub.payment.repository;

import com.libraryhub.payment.entity.UserSubscription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSubscriptionRepository extends JpaRepository<UserSubscription,Long> {
}
