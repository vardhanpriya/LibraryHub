package com.libraryhub.circulation.repository;

import com.libraryhub.catalog.entity.Book;
import com.libraryhub.circulation.entity.Reservation;
import com.libraryhub.circulation.enums.ReservationStatus;
import com.libraryhub.common.entity.LibraryBranch;
import com.libraryhub.identity.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation,Long> {

    @Query("""
        SELECT COALESCE(MAX(r.priority), 0)
        FROM Reservation r
        WHERE r.book = :book
        AND r.branch = :branch
        AND r.status IN (:statuses)
    """)
    Integer getMaxPriority( @Param("book") Book book, @Param("branch") LibraryBranch branch,
                            @Param("statuses") List<ReservationStatus> statuses);

    boolean existsByUserAndBookAndStatusIn(User user, Book book, List<String> active);

    List<Reservation> findByUser(User findyById);

    @Query("""
    SELECT r FROM Reservation r
    WHERE (:userId IS NULL OR r.user.id = :userId)
      AND (:bookId IS NULL OR r.book.bookId = :bookId)
      AND (:branchId IS NULL OR r.branch.branchId = :branchId)
      AND (:status IS NULL OR r.status = :status)
      AND (:minPriority IS NULL OR r.priority >= :minPriority)
      AND (:maxPriority IS NULL OR r.priority <= :maxPriority)
   AND (r.reservationDate >= COALESCE(:fromDate, r.reservationDate))
                 AND (r.reservationDate <= COALESCE(:toDate, r.reservationDate))
""")
    List<Reservation> searchReservations(
          @Param("userId")  Long userId,
          @Param("bookId")  Long bookId,
          @Param("branchId")  Long branchId,
          @Param("status")  ReservationStatus status,
          @Param("minPriority")  Integer minPriority,
          @Param("maxPriority")  Integer maxPriority,
          @Param("fromDate")  LocalDate fromDate,
          @Param("toDate")  LocalDate toDate
    );
}
