package com.libraryhub.circulation.service.impl;

import com.libraryhub.catalog.entity.Book;
import com.libraryhub.catalog.repository.BookRepository;
import com.libraryhub.circulation.entity.Reservation;
import com.libraryhub.circulation.enums.ReservationStatus;
import com.libraryhub.circulation.repository.ReservationRepository;
import com.libraryhub.circulation.service.ReservationService;
import com.libraryhub.common.entity.LibraryBranch;
import com.libraryhub.common.repository.LibraryBranchRepository;
import com.libraryhub.identity.entity.User;
import com.libraryhub.identity.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final LibraryBranchRepository branchRepository;
    @Override
    @Transactional
    public Reservation createReservation(Long userId, Long bookId, Long branchId) {
        /*
        User user = userRepository.getReferenceById(userId);
Book book = bookRepository.getReferenceById(bookId);
LibraryBranch branch = branchRepository.getReferenceById(branchId);
         */
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        LibraryBranch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new RuntimeException("Branch not found"));

        boolean exists = reservationRepository
                .existsByUserAndBookAndStatusIn(
                        user,
                        book,
                        List.of("ACTIVE", "READY_FOR_PICKUP")
                );

        if (exists) {
            throw new RuntimeException("Active reservation already exists");
        }

        Integer maxPriority = reservationRepository
                .getMaxPriority(book, branch);

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setBook(book);
        reservation.setBranch(branch);
        reservation.setReservationDate(LocalDate.now());
        reservation.setExpiryDate(LocalDate.now().plusDays(7));
        reservation.setPriority(maxPriority + 1);
        reservation.setStatus(ReservationStatus.ACTIVE);
        reservation.setCreatedAt(LocalDateTime.now());

        return reservationRepository.save(reservation);
    }

    @Override
    @Transactional
    public void cancelReservation(Long reservationId, Long userId) {

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Not found"));

//        if (!reservation.getUserId().equals(userId)) {
//            throw new RuntimeException("Unauthorized");
//        }

        if (reservation.getStatus() != ReservationStatus.ACTIVE &&
                reservation.getStatus() != ReservationStatus.READY_FOR_PICKUP) {
            throw new RuntimeException("Cannot cancel reservation");
        }

        reservation.setStatus(ReservationStatus.CANCELLED);
    }
}
