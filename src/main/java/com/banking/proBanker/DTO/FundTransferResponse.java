package com.banking.proBanker.DTO;

public record FundTransferResponse (String sourceAccountNumber, String targetAccountNumber, double amount, String pin){
}
