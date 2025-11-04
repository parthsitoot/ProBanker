package com.banking.proBanker.Repository;

import com.banking.proBanker.Entity.Account;
import com.banking.proBanker.Entity.User;
import org.springframework.boot.webmvc.autoconfigure.WebMvcProperties;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByAccountAccountNumber (String accountNumber);
    Optional<User> findByEmail (String email);
    Optional<User> findByPhoneNumber (String phoneNumber);

}
