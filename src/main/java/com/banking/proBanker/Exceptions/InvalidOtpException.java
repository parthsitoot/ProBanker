package com.banking.proBanker.Exceptions;

public class InvalidOtpException extends RuntimeException {

    public InvalidOtpException(String msg) {
        super(msg);
    }
}
