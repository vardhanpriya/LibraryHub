//package com.libraryhub.circulation.service.impl;
//
//import com.libraryhub.circulation.entity.Loan;
//import com.libraryhub.circulation.entity.LoanPolicy;
//import com.libraryhub.circulation.repository.FineRepository;
//import com.libraryhub.circulation.repository.LoanPolicyRepository;
//import com.libraryhub.circulation.repository.LoanRepository;
//import com.libraryhub.circulation.repository.ReservationRepository;
//import com.libraryhub.circulation.service.LoanService;
//import com.libraryhub.inventory.repository.BookCopyRepository;
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDate;
//import java.util.List;
//
//
//@Service
//@RequiredArgsConstructor
//public class LoanServiceImpl implements LoanService {
//
//    private final LoanRepository loanRepository;
//    private final LoanPolicyRepository policyRepository;
//    private final BookCopyRepository copyRepository;
//    private final FineRepository fineRepository;
//    private final ReservationRepository repository;
//
//    @Transactional
//    @Override
//    public Loan createLoan(Long userId,
//                           Long reservationId,
//                           Long branchId) {
//
//        if (loanRepository.existsByCopyIdAndReturnDateIsNull(copyId)) {
//            throw new RuntimeException("Copy already loaned");
//        }
//
//        LoanPolicy policy = policyRepository
//                .findActivePolicy(branchId)
//                .orElseThrow(() -> new RuntimeException("No policy found"));
//
//        LocalDate issueDate = LocalDate.now();
//        LocalDate dueDate = issueDate.plusDays(policy.getLoanDays());
//
//        Loan loan = new Loan();
//        loan.setUserId(userId);
//        loan.setCopyId(copyId);
//        loan.setBranchId(branchId);
//        loan.setIssueDate(issueDate);
//        loan.setDueDate(dueDate);
//        loan.setStatus(LoanStatus.ACTIVE);
//        loan.setLoanPolicyId(policy.getPolicyId());
//
//        copyRepository.markAsLoaned(copyId);
//
//        return loanRepository.save(loan);
//    }
//
//    @Transactional
//    @Override
//    public void returnLoan(Long loanId) {
//
//        Loan loan = loanRepository.findById(loanId)
//                .orElseThrow(() -> new RuntimeException("Loan not found"));
//
//        if (loan.getReturnDate() != null) {
//            throw new RuntimeException("Already returned");
//        }
//
//        LocalDate returnDate = LocalDate.now();
//        loan.setReturnDate(returnDate);
//        loan.setStatus(LoanStatus.RETURNED);
//
//        calculateFineIfNeeded(loan, returnDate);
//    }
//
//
//
//    private void calculateFineIfNeeded(Loan loan,
//                                       LocalDate returnDate) {
//
//        LoanPolicy policy = policyRepository
//                .findById(loan.getLoanPolicyId())
//                .orElseThrow();
//
//        long overdueDays =
//                ChronoUnit.DAYS.between(
//                        loan.getDueDate().plusDays(policy.getGraceDays()),
//                        returnDate
//                );
//
//        if (overdueDays > 0) {
//
//            BigDecimal fineAmount =
//                    policy.getFinePerDay()
//                            .multiply(BigDecimal.valueOf(overdueDays));
//
//            Fine fine = new Fine();
//            fine.setLoanId(loan.getLoanId());
//            fine.setAmount(fineAmount);
//            fine.setStatus(FineStatus.UNPAID);
//
//            fineRepository.save(fine);
//        }
//    }
//
//
//    @Override
//    public List<Loan> getLoansByUser(Long userId) {
//        return loanRepository.findByUserId(userId);
//    }
//}
