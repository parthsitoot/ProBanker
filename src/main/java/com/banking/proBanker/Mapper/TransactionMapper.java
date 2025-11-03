package com.banking.proBanker.Mapper;

import com.banking.proBanker.DTO.TransactionDto;
import com.banking.proBanker.Entity.Transaction;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {
    public TransactionDto toDto (Transaction transaction) {
        return new TransactionDto(transaction);
    }
}
