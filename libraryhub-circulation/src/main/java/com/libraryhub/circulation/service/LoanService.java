package com.libraryhub.circulation.service;

import com.libraryhub.circulation.entity.Loan;
import com.libraryhub.circulation.response.CreateLoanResponse;

import java.time.LocalDate;
import java.util.List;

public interface LoanService {

    CreateLoanResponse createLoan(Long reservationId);

    void returnLoan(Long loanId);


    List<Loan> getLoansByUser(Long userId);
}
