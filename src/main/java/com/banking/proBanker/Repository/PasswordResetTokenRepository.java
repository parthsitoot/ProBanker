package com.banking.proBanker.Repository;

import com.banking.proBanker.Entity.PasswordResetToken;
import com.banking.proBanker.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken (String token);
    PasswordResetToken findByUser (User user);
    void deleteByToken (String token);
}
