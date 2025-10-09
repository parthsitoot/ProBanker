package com.banking.proBanker.Repository;

import com.banking.proBanker.Entity.Account;
import com.banking.proBanker.Entity.User;
import org.springframework.boot.webmvc.autoconfigure.WebMvcProperties;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByAccountNumber (String accountNumber);
    Optional<User> findByEmail (String email);
    Optional<User> findByPhoneNumber (String phoneNumber);

}
