package com.banking.proBanker.Exceptions;

public class   InvalidPinException extends RuntimeException {

    public InvalidPinException(String message) {
        super(message);
    }
}
