package com.libraryhub.circulation.service;

import com.libraryhub.circulation.entity.Loan;

import java.time.LocalDate;
import java.util.List;

public interface LoanService {

    Loan createLoan(Long userId,
                    Long copyId,
                    Long branchId);

    void returnLoan(Long loanId);


    List<Loan> getLoansByUser(Long userId);
}
