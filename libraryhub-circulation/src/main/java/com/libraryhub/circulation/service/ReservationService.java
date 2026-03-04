package com.libraryhub.circulation.service;

import com.libraryhub.circulation.entity.Reservation;
import com.libraryhub.circulation.enums.ReservationStatus;
import com.libraryhub.circulation.response.CreateReservationResponse;

import java.time.LocalDate;
import java.util.List;

public interface ReservationService {
     CreateReservationResponse createReservation(Long userId, Long bookId, Long branchId);

     void cancelReservation(Long reservationId, Long userId);

     List<CreateReservationResponse> getAllReservationOfUser(Long userId);

     CreateReservationResponse getReservationById(Long id);

     List<CreateReservationResponse> searchReservations(Long userId, Long bookId, Long branchId,
                                                        ReservationStatus status,
                                                        Integer minPriority, Integer maxPriority,
                                                        LocalDate fromDate,
                                                        LocalDate toDate);
}
