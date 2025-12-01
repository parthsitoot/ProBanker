package com.banking.proBanker.DTO;

import com.banking.proBanker.Entity.TransactionType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.val;
import com.banking.proBanker.Entity.*;
import java.util.Date;

@Data
@NoArgsConstructor
public class TransactionDto {
    private Long id;
    private double amount;
    private TransactionType transactionType;
    private Date transactionDate;
    private String sourceAccountNumber;
    private String targetAccountNumber;

    public TransactionDto(Transaction transaction) {
        this.id = transaction.getId();
        this.amount = transaction.getAmount();
        this.transactionType = transaction.getTransactionType();
        this.transactionDate = transaction.getTransactionDate();
        this.sourceAccountNumber = transaction.getSourceAccount().getAccountNumber();

        val targetAccount = transaction.getTargetAccount();
        var targetAccountNumber = "N/A";
        if (targetAccount != null) {
            targetAccountNumber = targetAccount.getAccountNumber();
        }
        this.targetAccountNumber = targetAccountNumber;
    }
}
