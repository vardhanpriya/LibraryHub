//package com.libraryhub.api.scheduler;
//
//
//import com.libraryhub.circulation.entity.Fine;
//import com.libraryhub.circulation.entity.Loan;
//import com.libraryhub.circulation.entity.LoanPolicy;
//import com.libraryhub.circulation.repository.FineRepository;
//import com.libraryhub.circulation.repository.HolidayRepository;
//import com.libraryhub.circulation.repository.LoanPolicyRepository;
//import com.libraryhub.circulation.repository.LoanRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.time.temporal.ChronoUnit;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class FineScheduler {
//
//    private final LoanRepository loanRepository;
//    private final FineRepository fineRepository;
//    private final LoanPolicyRepository loanPolicyRepository;
//    private final HolidayRepository holidayRepository;
//
//    @Scheduled(cron = "0 0 0 * * ?") // runs every day at midnight
//    public void calculateFinesForOverdueLoans() {
//        LocalDate today = LocalDate.now();
//
//        // Fetch loans which are overdue and not returned
//        List<Loan> overdueLoans = loanRepository.findByDueDateBeforeAndReturnDateIsNull(today);
//
//        for (Loan loan : overdueLoans) {
//            LoanPolicy policy = loanPolicyRepository.findEffectivePolicy(loan.getBranch(), loan.getUser().getRole())
//                    .orElseThrow(() -> new RuntimeException("No loan policy found"));
//
//            // Calculate overdue days considering holidays
//            long overdueDays = ChronoUnit.DAYS.between(loan.getDueDate(), today);
//
//            // Fetch holidays for branch
//            List<LocalDate> holidays = holidayRepository.findByBranchOrGlobal(loan.getBranch())
//                    .stream()
//                    .map(Holiday::getDate)
//                    .toList();
//
//            long fineDays = overdueDays - holidays.stream().filter(d -> d.isAfter(loan.getDueDate())).count();
//            if (fineDays <= 0) continue;
//
//            BigDecimal fineAmount = policy.getFinePerDay().multiply(BigDecimal.valueOf(fineDays));
//
//            // Check if fine already exists for this loan
//            Fine fine = fineRepository.findByLoan(loan)
//                    .orElse(new Fine());
//
//            fine.setLoan(loan);
//            fine.setAmount(fineAmount);
//            fine.setPaid(false);
//            fine.setCreatedAt(LocalDateTime.now());
//
//            fineRepository.save(fine);
//        }
//    }
//
//}
