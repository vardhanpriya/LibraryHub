//package com.libraryhub.api.controller.circulation;
//
//
//import com.libraryhub.circulation.entity.Loan;
//import com.libraryhub.circulation.request.CreateLoanRequest;
//import com.libraryhub.circulation.service.LoanService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/v1/loans")
//@RequiredArgsConstructor
//public class LoanController {
//
//    private final LoanService loanService;
//
//    @PostMapping("/create-loan")
//    public ResponseEntity<?> create(@RequestBody CreateLoanRequest request) {
//
//        Loan loan = loanService.createLoan(
//                request.getUserId(),
//                request.reservationId(),
//                request.getBranchId()
//        );
//
//        return ResponseEntity.ok(loan);
//    }
//
//    @PatchMapping("/{loanId}/return")
//    public ResponseEntity<?> returnBook(@PathVariable Long loanId) {
//
//        loanService.returnLoan(loanId);
//        return ResponseEntity.ok("Returned successfully");
//    }
//
//    @GetMapping("/user/{userId}")
//    public ResponseEntity<?> getByUser(@PathVariable Long userId) {
//
//        return ResponseEntity.ok(
//                loanService.getLoansByUser(userId)
//        );
//    }
//}
