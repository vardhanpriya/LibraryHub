package com.libraryhub.circulation.service;

import com.libraryhub.circulation.entity.Reservation;

public interface ReservationService {
     Reservation createReservation(Long userId, Long bookId, Long branchId);

     void cancelReservation(Long reservationId, Long userId);
}
