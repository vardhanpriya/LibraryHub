package com.libraryhub.circulation.repository;

import com.libraryhub.circulation.entity.Fine;
import com.libraryhub.circulation.entity.LoanPolicy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanPolicyRepository extends JpaRepository<LoanPolicy,Long> {
}
