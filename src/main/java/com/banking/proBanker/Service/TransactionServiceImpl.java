package com.banking.proBanker.Service;

import com.banking.proBanker.DTO.TransactionDto;
import com.banking.proBanker.Mapper.TransactionMapper;
import com.banking.proBanker.Repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService{
    private final TransactionMapper transactionMapper;
    private final TransactionRepository transactionRepository;

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
