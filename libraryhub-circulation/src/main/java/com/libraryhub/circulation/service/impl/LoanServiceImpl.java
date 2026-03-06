package com.libraryhub.circulation.service.impl;

import com.libraryhub.circulation.entity.Fine;
import com.libraryhub.circulation.entity.Loan;
import com.libraryhub.circulation.entity.LoanPolicy;
import com.libraryhub.circulation.entity.Reservation;
import com.libraryhub.circulation.enums.BookCopyStatus;
import com.libraryhub.circulation.enums.FineStatus;
import com.libraryhub.circulation.enums.LoanStatus;
import com.libraryhub.circulation.enums.ReservationStatus;
import com.libraryhub.circulation.repository.FineRepository;
import com.libraryhub.circulation.repository.LoanPolicyRepository;
import com.libraryhub.circulation.repository.LoanRepository;
import com.libraryhub.circulation.repository.ReservationRepository;
import com.libraryhub.circulation.response.CreateLoanResponse;
import com.libraryhub.circulation.service.LoanService;
import com.libraryhub.circulation.utility.ResolveLoanPolicyService;
import com.libraryhub.inventory.entity.BookCopy;
import com.libraryhub.inventory.repository.BookCopyRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;
    private final LoanPolicyRepository policyRepository;
    private final BookCopyRepository bookCopyRepository;
    private final FineRepository fineRepository;
    private final ReservationRepository reservationRepository;
    private final ResolveLoanPolicyService loanPolicyService;

    @Transactional
    @Override
    public CreateLoanResponse createLoan(Long reservationId) {

        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(()->new RuntimeException("Reservation not found with given reservationId"));
//        LoanPolicy policy = policyRepository
//                .findActivePolicy(branchId)
//                .orElseThrow(() -> new RuntimeException("No policy found"));


        if (reservation.getStatus() != ReservationStatus.READY_FOR_PICKUP &&
                reservation.getStatus() != ReservationStatus.ACTIVE) {
            throw new RuntimeException("Reservation is not valid for loan creation");
        }
        if (reservation.getExpiryDate() != null &&
                reservation.getExpiryDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("Reservation has expired");
        }

        Long bookId = reservation.getBook().getBookId();
        Long branchId = reservation.getBranch().getBranchId();

        LoanPolicy loanPolicy = loanPolicyService.resolveLoanPolicy(reservation.getUser(), reservation.getBranch());

        BookCopy bookCopy = bookCopyRepository
                .findFirstByBook_BookIdAndBranch_BranchIdAndStatus(bookId, branchId, "AVAILABLE")
                .orElseThrow(() -> new RuntimeException("No available copy found for this book in branch"));

        Loan loan = new Loan();
        loan.setUser(reservation.getUser());
        loan.setBookCopy(bookCopy);
        loan.setBranch(reservation.getBranch());
        loan.setIssueDate(LocalDate.now());
        loan.setDueDate(LocalDate.now().plusDays(loanPolicy.getLoanDays())); // as per policy loan days
        loan.setStatus(LoanStatus.ACTIVE);
        loan.setLoanPolicy(loanPolicy);
        loan.setCreatedAt(LocalDateTime.now());

        Loan savedLoan = loanRepository.save(loan);

        // update bookCopy table
        bookCopy.setStatus("LOANED"); // AVAILABLE, LOANED, DAMAGED
        bookCopyRepository.save(bookCopy);

        //update reservation status
        reservation.setStatus(ReservationStatus.FULFILLED);
        reservationRepository.save(reservation);

        // create loan Response
        CreateLoanResponse response = new CreateLoanResponse();
        response.setLoanId(savedLoan.getLoanId());
        response.setBookCopyBarcode(bookCopy.getBarcode());
        response.setDueDate(loan.getDueDate());
        return response;
    }

    @Transactional
    @Override
    public void returnLoan(Long loanId) {

        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found with id : "+loanId));

        if (loan.getReturnDate() != null) {
            throw new RuntimeException("This Loan is Already returned : ID : " + loanId);
        }

        LocalDate returnDate = LocalDate.now();
        loan.setReturnDate(returnDate);
        loan.setStatus(LoanStatus.RETURNED);

        BookCopy copy = loan.getBookCopy();

        calculateAndSaveFineIfNeeded(loan, returnDate);

        Optional<Reservation> nextReservation =
                reservationRepository.findTopByBookAndBranchAndStatusOrderByPriorityAsc(
                        loan.getBookCopy().getBook(),
                        loan.getBranch(),
                        ReservationStatus.ACTIVE
                );

        if (nextReservation.isPresent()) {

            Reservation reservation = nextReservation.get();
            reservation.setStatus(ReservationStatus.READY_FOR_PICKUP);
            reservation.setExpiryDate(LocalDate.now().plusDays(2));
            copy.setStatus("RESERVED");
        } else {
            copy.setStatus("AVAILABLE");
        }
        bookCopyRepository.save(copy);
        loanRepository.save(loan);
    }



    private void calculateAndSaveFineIfNeeded(Loan loan,
                                       LocalDate returnDate) {

        LoanPolicy policy = loan.getLoanPolicy();

        long overdueDays =
                ChronoUnit.DAYS.between(loan.getDueDate().plusDays(policy.getGraceDays()), returnDate);

        if (overdueDays > 0) {

            BigDecimal fineAmount = policy.getFinePerDay().multiply(BigDecimal.valueOf(overdueDays));

            Fine fine = new Fine();
            fine.setLoan(loan);
            fine.setAmount(fineAmount);
            fine.setStatus(FineStatus.UNPAID);
            fine.setCreatedAt(LocalDateTime.now());
            fineRepository.save(fine);
        }
    }


    @Override
    public List<Loan> getLoansByUser(Long userId) {
        return loanRepository.findByUserId(userId);
    }
}
