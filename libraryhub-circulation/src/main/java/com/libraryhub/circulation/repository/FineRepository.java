package com.libraryhub.circulation.repository;

import com.libraryhub.circulation.entity.Fine;
import com.libraryhub.identity.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FineRepository  extends JpaRepository<Fine,Long> {
    List<Fine> findByLoanUserAndPaidFalse(User user);
}
