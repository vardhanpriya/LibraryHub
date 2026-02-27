package com.libraryhub.circulation.repository;

import com.libraryhub.circulation.entity.Fine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FineRepository  extends JpaRepository<Fine,Long> {
}
