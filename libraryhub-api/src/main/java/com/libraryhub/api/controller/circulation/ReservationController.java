package com.libraryhub.api.controller.circulation;

import com.libraryhub.api.response.ApiResponse;
import com.libraryhub.api.response.ApiResponseBuilder;
import com.libraryhub.circulation.enums.ReservationStatus;
import com.libraryhub.circulation.request.CreateReservationRequest;
import com.libraryhub.circulation.response.CreateReservationResponse;
import com.libraryhub.circulation.service.ReservationService;
import com.libraryhub.identity.security.CustomUserDetails;
import com.sun.security.auth.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/v1/reservations")
@RequiredArgsConstructor
public class ReservationController {


    private final ReservationService reservationService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<CreateReservationResponse>> create(@RequestBody CreateReservationRequest request,
                                                                        @AuthenticationPrincipal CustomUserDetails user ) {

        CreateReservationResponse response = reservationService.createReservation(
                user.getUserId(), request.getBookId(),request.getBranchId());

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseBuilder.success(response));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<String>> cancel(@PathVariable Long id,
                                    @AuthenticationPrincipal CustomUserDetails user) {

        reservationService.cancelReservation(id, user.getUserId());
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseBuilder.success("Cancelled Reservation.."));
    }

    @GetMapping("/get-all-reservation")
    public ResponseEntity<ApiResponse<List<CreateReservationResponse>>> getAlReservationOfUser(@AuthenticationPrincipal CustomUserDetails user) {

        List<CreateReservationResponse> resp =  reservationService.getAllReservationOfUser( user.getUserId());
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseBuilder.success(resp));
    }


    @GetMapping("/get-reservation-by-id/{id}")
    public ResponseEntity<ApiResponse<CreateReservationResponse>> getReservationByReservationId(@PathVariable Long id) {

        CreateReservationResponse resp =  reservationService.getReservationById( id);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseBuilder.success(resp));
    }

    @PostMapping("/partial-search")
    public ResponseEntity<ApiResponse<List<CreateReservationResponse>>> search(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false)  Long bookId,
            @RequestParam(required = false)  Long branchId,
            @RequestParam(required = false) ReservationStatus status,
            @RequestParam(required = false)  Integer minPriority,
            @RequestParam(required = false) Integer maxPriority,
            @RequestParam(required = false) LocalDate fromDate,
            @RequestParam(required = false)  LocalDate toDate) {

        List<CreateReservationResponse> response =
                reservationService.searchReservations(userId,bookId,branchId,status,minPriority,maxPriority,fromDate,toDate);

        return ResponseEntity.ok(ApiResponseBuilder.success(response));
    }
}
