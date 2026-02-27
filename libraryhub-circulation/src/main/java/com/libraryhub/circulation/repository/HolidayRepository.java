package com.libraryhub.circulation.repository;

import com.libraryhub.circulation.entity.Fine;
import com.libraryhub.circulation.entity.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HolidayRepository extends JpaRepository<Holiday,Long> {
}
