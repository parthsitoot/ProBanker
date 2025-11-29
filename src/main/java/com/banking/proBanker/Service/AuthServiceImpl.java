package com.banking.proBanker.Service;

import com.banking.proBanker.DTO.OtpRequest;
import com.banking.proBanker.DTO.OtpVerificationRequest;
import com.banking.proBanker.DTO.ResetPasswordRequest;
import com.banking.proBanker.Entity.PasswordResetToken;
import com.banking.proBanker.Entity.User;
import com.banking.proBanker.Repository.PasswordResetTokenRepository;
import com.banking.proBanker.Utilities.ApiMessages;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpStatus;
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
        log.info("Received otp request for Identifier: {}", otpRequest.identifier());
        val user = userService.getUserByIdentifier(otpRequest.identifier());
        val accountNumber = user.getAccount().getAccountNumber();
        val generatedOtp = otpService.generateOtp(accountNumber);
        return sendOtpEmail(user, accountNumber, generatedOtp);
    }

    @Override
    public ResponseEntity<String> verifyOtpAndIssueResetToken(OtpVerificationRequest otpVerificationRequest) {
        validateOtpRequest(otpVerificationRequest);

        val user = userService.getUserByIdentifier(otpVerificationRequest.identifier());
        val accountNumber = user.getAccount().getAccountNumber();

        if (!otpService.validateOTP(accountNumber, otpVerificationRequest.otp())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiMessages.OTP_INVALID_ERROR.getMessage());
        }

        String resetToken = generatePasswordResetToken(user);
        return ResponseEntity.ok(String.format(ApiMessages.PASSWORD_RESET_TOKEN_ISSUED.getMessage(), resetToken));
    }

    @Override
    public ResponseEntity<String> resetPassword (ResetPasswordRequest resetPasswordRequest) {
        val user = userService.getUserByIdentifier(resetPasswordRequest.identifier());

        if (!verifyPasswordResetToken(resetPasswordRequest.resetToken(), user)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiMessages.TOKEN_INVALID_ERROR.getMessage());
        }

        try {
            boolean passwordResetSuccessful = userService.resetPassword(user, resetPasswordRequest.newPassword());
            if (passwordResetSuccessful) {
                return ResponseEntity.ok (ApiMessages.PASSWORD_RESET_SUCCESS.getMessage());
            } else {
                return ResponseEntity.internalServerError().body(ApiMessages.PASSWORD_RESET_FAILURE.getMessage());
            }
        } catch (Exception e) {
            log.error("Error in resetting password: {}", e.getMessage());
            return ResponseEntity.internalServerError().body(ApiMessages.PASSWORD_RESET_FAILURE.getMessage());
        }
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

    public void validateOtpRequest (OtpVerificationRequest otpVerificationRequest) {
        if (otpVerificationRequest.identifier() == null || otpVerificationRequest.identifier().isEmpty()) {
            throw new IllegalArgumentException(ApiMessages.IDENTIFIER_MISSING_ERROR.getMessage());
        }
        if (otpVerificationRequest.otp() == null || otpVerificationRequest.otp().isEmpty()) {
            throw new IllegalArgumentException(ApiMessages.OTP_MISSING_ERROR.getMessage());
        }
    }
}
