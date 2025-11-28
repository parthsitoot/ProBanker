package com.banking.proBanker.Service;

import com.banking.proBanker.DTO.TransactionDto;

import java.util.List;

public interface TransactionService {
    public List<TransactionDto> getAllTransactionsByAccountNumber (String accountNumber);
}
