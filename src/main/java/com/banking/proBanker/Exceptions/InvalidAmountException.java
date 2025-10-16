package com.banking.proBanker.Exceptions;

public class InvalidAmountException extends RuntimeException {
    
    public InvalidAmountException(String message) {
        super(message);
    }
}
