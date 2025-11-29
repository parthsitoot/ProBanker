package com.banking.proBanker.Service;

import com.banking.proBanker.DTO.TransactionDto;
import com.banking.proBanker.Mapper.TransactionMapper;
import com.banking.proBanker.Repository.TransactionRepository;
import com.banking.proBanker.Utilities.ValidateUtil;
import lombok.val;

import java.util.List;

public class TransactionServiceImpl implements TransactionService{
    private TransactionMapper transactionMapper;
    private TransactionRepository transactionRepository;
    @Override
    public List<TransactionDto> getAllTransactionsByAccountNumber(String accountNumber) {
        val transactions = transactionRepository
                .findBySourceAccount_AccountNumberOrTargetAccount_AccountNumber(accountNumber, accountNumber);

        return transactions.parallelStream()
                .map(transactionMapper::toDto)
                .sorted((t1, t2) -> t1.getTransactionDate().compareTo(t2.getTransactionDate()))
                .toList();
    }
}
