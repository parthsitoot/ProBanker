package com.banking.proBanker.Repository;

import com.banking.proBanker.Entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Transaction findBySourceAccount_AccountNumberOrTargetAccount_AccountNumber (String sourceAccountNumber, String targetAccountNumber);
}
