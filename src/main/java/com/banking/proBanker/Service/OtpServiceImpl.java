package com.banking.proBanker.Service;

import com.banking.proBanker.Entity.OtpInfo;
import com.banking.proBanker.Repository.OtpInfoRepository;
import com.banking.proBanker.Utilities.ValidateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService{
    public static final int OTP_ATTEMPTS_LIMIT = 3;
    public static final int OTP_EXPIRY_MINUTES = 5;
    public static final int OTP_RESET_WAITING_TIME_MINUTES = 10;
    public static final int OTP_RETRY_LIMIT_WINDOW_MINUTES = 15;

    private final CacheManager cacheManager;
    private final EmailService emailService;
    private final OtpInfoRepository otpInfoRepository;
    private final ValidateUtil validationUtil;

    private LocalDateTime otpLimitReachedTime = null;
    @Override
    public CompletableFuture<Void> sendOTPByEmail(String email, String name, String accountNumber, String otp) {

    }

    @Override
    public boolean validateOTP(String accountNumber, String otp) {
        return false;
    }

    private void validateOtpWithinRetryLink (OtpInfo otpInfo) {
        if () {

        }
    }

    private boolean isOtpRetryLimitExceeded(OtpInfo otpInfo) {
        val attempts = getOtpAttempts(otpInfo.getAccountNumber());
        if (attempts < OTP_ATTEMPTS_LIMIT) {
            return false;
        }

        if (isOtpResetWaitingTimeExceeded()) {
            resetOtpAttempts(otpInfo.getAccountNumber());
            return false;
        }

        val now = LocalDateTime.now();

        return otpInfo.getGeneratedAt().isAfter(now.minusMinutes(OTP_RETRY_LIMIT_WINDOW_MINUTES));

    }

    private void resetOtpAttempts (String accountNumber) {
        otpLimitReachedTime = null;
        val cache = cacheManager.getCache("otpAttempts");
        if (cache != null) {
            cache.put(accountNumber, 0);
        }

    }
    private int getOtpAttempts (String accountNumber) {
        var attempts = 0;
        val cache = cacheManager.getCache("otpAttempts");
        if (cache == null) {
            return attempts;
        }

        val value = cache.get(accountNumber, Integer.class);
        if (value != null) {
            attempts = value;
        }

        return attempts;
    }
    private boolean isOtpResetWaitingTimeExceeded() {
        val now = LocalDateTime.now();
        return otpLimitReachedTime != null
                && otpLimitReachedTime.isBefore(now.minusMinutes(OTP_RESET_WAITING_TIME_MINUTES));
    }
}
