package com.banking.proBanker.Service;

import java.util.concurrent.CompletableFuture;

public interface OtpService {
    public CompletableFuture<Void> sendOTPByEmail(String email, String name, String accountNumber, String otp) ;
    public boolean validateOTP(String accountNumber, String otp);
}
