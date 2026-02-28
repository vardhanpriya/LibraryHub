//package com.libraryhub.api.controller.circulation;
//
//import com.libraryhub.circulation.service.ReservationService;
//import com.sun.security.auth.UserPrincipal;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/v1/reservations")
//@RequiredArgsConstructor
//public class ReservationController {
//
//
//    private final ReservationService reservationService;
//
//
//    @PostMapping
//    public ResponseEntity<ReservationResponse> create(
//            @RequestBody CreateReservationRequest request,
//            @AuthenticationPrincipal UserPrincipal user) {
//
//        ReservationResponse response =
//                reservationService.createReservation(
//                        user.getUserId(),
//                        request
//                );
//
//        return ResponseEntity.ok(response);
//    }
//
//    @PatchMapping("/{id}/cancel")
//    public ResponseEntity<?> cancel(@PathVariable Long id,
//                                    @AuthenticationPrincipal UserPrincipal user) {
//
//        reservationService.cancelReservation(id, user.getUserId());
//        return ResponseEntity.ok("Cancelled successfully");
//    }
//}
