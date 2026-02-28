package com.libraryhub.circulation.repository;

import com.libraryhub.circulation.entity.Fine;
import com.libraryhub.circulation.entity.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface HolidayRepository extends JpaRepository<Holiday,Long> {
    boolean existsByDateAndBranchIsNull(LocalDate date);

    boolean existsByDateAndBranchBranchId(LocalDate date, Long branchId);

    List<Holiday> findByStatus(String status);

    List<Holiday> findByDateAndStatus(LocalDate date, String status);

    // For fetching holidays applicable to a branch
    @Query("""
    SELECT h FROM Holiday h
    WHERE h.status = :status
      AND h.date = :date
      AND (h.branch.branchId = :branchId OR h.branch IS NULL)
""")
    List<Holiday> findApplicableHoliday(
            @Param("status") String status,
            @Param("date") LocalDate date,
            @Param("branchId") Long branchId
    );
}
