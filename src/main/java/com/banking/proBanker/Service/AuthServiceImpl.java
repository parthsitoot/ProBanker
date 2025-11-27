package com.banking.proBanker.Service;

import com.banking.proBanker.DTO.OtpRequest;
import com.banking.proBanker.DTO.OtpVerificationRequest;
import com.banking.proBanker.Entity.PasswordResetToken;
import com.banking.proBanker.Entity.User;
import com.banking.proBanker.Repository.PasswordResetTokenRepository;
import com.banking.proBanker.Utilities.ApiMessages;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private static final int EXPIRATION_HOURS = 24;


    private final OtpService otpService;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final UserService userService;

    @Override
    public String generatePasswordResetToken(User user) {
        val existingToken = passwordResetTokenRepository.findByUser(user);
        if (existingToken != null) {
            return existingToken.getToken();
        }

        val token = UUID.randomUUID().toString();
        val expiry = LocalDateTime.now().plusHours(EXPIRATION_HOURS);
        val passwordResetToken = new PasswordResetToken(token, user, expiry);
        passwordResetTokenRepository.save(passwordResetToken);
        return token;
    }

    @Override
    public boolean verifyPasswordResetToken(String token, User user) {
        val passwordResentToken = passwordResetTokenRepository.findByToken(token);
        if (passwordResentToken.isEmpty()) {
            return false;
        }
        deletePasswordResetToken(token);
        return user.equals(passwordResentToken.get().getUser()) && passwordResentToken.get().isTokenValid();
    }

    @Override
    public void deletePasswordResetToken(String token) {
        passwordResetTokenRepository.deleteByToken(token);
    }

    @Override
    public ResponseEntity<String> sendOtpForPasswordReset(OtpRequest otpRequest) {
        return null;
    }

    @Override
    public ResponseEntity<String> verifyOtpAndIssueResetToken(OtpVerificationRequest otpVerificationRequest) {
        return null;
    }


    // additional functions
    private boolean isExistingTokenValid(PasswordResetToken existingToken) {
        return existingToken != null && existingToken.getExpiryDateTime().isAfter(LocalDateTime.now().plusMinutes(5));
    }

    private ResponseEntity<String> sendOtpEmail(User user, String accountNumber, String generatedOtp) {
        val emailSendingFuture = otpService.sendOTPByEmail(user.getEmail(), user.getName(), accountNumber,
                generatedOtp);

        val successResponse = ResponseEntity
                .ok(String.format(ApiMessages.OTP_SENT_SUCCESS.getMessage(), user.getEmail()));
        val failureResponse = ResponseEntity.internalServerError()
                .body(String.format(ApiMessages.OTP_SENT_FAILURE.getMessage(), user.getEmail()));

        return emailSendingFuture.thenApply(result -> successResponse)
                .exceptionally(e -> failureResponse).join();
    }
}
