package com.libraryhub.circulation.repository;

import com.libraryhub.circulation.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface LoanRepository extends JpaRepository<Loan,Long> {
    List<Loan> findByDueDateBeforeAndReturnDateIsNull(LocalDate today);
    List<Loan> findByUserId(Long userId);

    boolean existsByCopy_CopyIdAndReturnDateIsNull(Long copyId);
}
