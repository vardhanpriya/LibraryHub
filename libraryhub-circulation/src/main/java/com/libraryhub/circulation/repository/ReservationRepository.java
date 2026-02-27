package com.libraryhub.circulation.repository;

import com.libraryhub.circulation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation,Long> {
}
