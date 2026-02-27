package com.libraryhub.circulation.repository;

import com.libraryhub.circulation.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository<Loan,Long> {
}
