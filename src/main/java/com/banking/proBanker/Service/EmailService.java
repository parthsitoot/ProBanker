package com.banking.proBanker.Service;

import org.springframework.scheduling.annotation.Async;

import java.util.concurrent.CompletableFuture;

public interface EmailService {
    @Async
    public CompletableFuture<Void> sendEmail(String to, String subject, String text);

    public String getLoginEmailTemplate(String name, String loginTime, String loginLocation);

    public String getOtpLoginEmailTemplate(String name, String accountNumber, String otp);
}
