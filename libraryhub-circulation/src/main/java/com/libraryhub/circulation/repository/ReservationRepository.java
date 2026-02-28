package com.libraryhub.circulation.repository;

import com.libraryhub.catalog.entity.Book;
import com.libraryhub.circulation.entity.Reservation;
import com.libraryhub.circulation.enums.ReservationStatus;
import com.libraryhub.common.entity.LibraryBranch;
import com.libraryhub.identity.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation,Long> {

    @Query("""
        SELECT COALESCE(MAX(r.priority), 0)
        FROM Reservation r
        WHERE r.book = :book
        AND r.branch = :branch
        AND r.status IN ('ACTIVE','READY_FOR_PICKUP')
    """)
    Integer getMaxPriority(Book book, LibraryBranch branch);

    boolean existsByUserAndBookAndStatusIn(User user, Book book, List<String> active);

}
