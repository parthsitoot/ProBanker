package com.banking.proBanker.Repository;

import com.banking.proBanker.Entity.Account;
import com.banking.proBanker.Entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository {
    Token findByToken (String token);
    Token[] findByAccount (Account account);
    void deleteByToken(String token);
}
