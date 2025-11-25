package com.banking.proBanker.Service;

import java.util.concurrent.CompletableFuture;

public interface OtpService {
    CompletableFuture<Void> sendOTPByEmail(String email, String name, String accountNumber, String otp) ;
    boolean validateOTP(String accountNumber, String otp);

    String generateOtp(String accountNumber);
}
