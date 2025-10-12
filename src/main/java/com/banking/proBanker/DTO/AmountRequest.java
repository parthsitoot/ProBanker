package com.banking.proBanker.DTO;


public record AmountRequest(String accountNUmber, String pin, double amount) {
}
