package com.banking.proBanker.DTO;

public record PinUpdateRequest(String accountNumber, String oldPin, String newPin, String password){
}
