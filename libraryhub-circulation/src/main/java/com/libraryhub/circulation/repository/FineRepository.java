package com.libraryhub.circulation.repository;

import com.libraryhub.circulation.entity.Fine;
import com.libraryhub.identity.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


@Repository
public interface FineRepository  extends JpaRepository<Fine,Long> {


    // Get all fines for a given user
    @Query("SELECT f FROM Fine f WHERE f.loan.user = :user")
    List<Fine> findByUser(@Param("user") User user);

    // Optionally, get only unpaid fines (status UNPAID or partially paid)
    @Query("SELECT f FROM Fine f WHERE f.loan.user = :user AND f.status IN ('UNPAID','PARTIALLY_PAID')")
    List<Fine> findUnpaidByUser(@Param("user") User user);
}
