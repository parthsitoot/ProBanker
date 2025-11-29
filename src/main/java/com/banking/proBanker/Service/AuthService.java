package com.banking.proBanker.Service;

import com.banking.proBanker.DTO.OtpRequest;
import com.banking.proBanker.DTO.OtpVerificationRequest;
import com.banking.proBanker.DTO.ResetPasswordRequest;
import com.banking.proBanker.Entity.User;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    public String generatePasswordResetToken(User user);

    public boolean verifyPasswordResetToken(String token, User user);

    public void deletePasswordResetToken(String token);

    public ResponseEntity<String> sendOtpForPasswordReset(OtpRequest otpRequest);

    public ResponseEntity<String> verifyOtpAndIssueResetToken(OtpVerificationRequest otpVerificationRequest);

    public ResponseEntity<String> resetPassword (ResetPasswordRequest resetPasswordRequest);
}
