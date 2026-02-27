package com.libraryhub.identity.repository;

import com.libraryhub.identity.entity.EmailVerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailVerificationTokenRepo extends JpaRepository<EmailVerificationToken,Long> {

    Optional<EmailVerificationToken> findByToken(String token);
}
