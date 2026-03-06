package com.libraryhub.api.controller.circulation;


import com.libraryhub.api.response.ApiResponse;
import com.libraryhub.api.response.ApiResponseBuilder;
import com.libraryhub.circulation.response.CreateLoanResponse;
import com.libraryhub.circulation.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;

    @PostMapping("/create-loan/{reservationId}")
    public ResponseEntity<ApiResponse<CreateLoanResponse>> create(@PathVariable Long reservationId ) {

        CreateLoanResponse loanResponse = loanService.createLoan(reservationId);

        return  ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseBuilder.success(loanResponse));
    }

    @PatchMapping("/{loanId}/return")
    public ResponseEntity<ApiResponse<String>> returnBook(@PathVariable Long loanId) {

        loanService.returnLoan(loanId);
        return  ResponseEntity.status(HttpStatus.OK).body(ApiResponseBuilder.success("Returned successfully"));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getByUser(@PathVariable Long userId) {

        return ResponseEntity.ok(
                loanService.getLoansByUser(userId)
        );
    }
}
