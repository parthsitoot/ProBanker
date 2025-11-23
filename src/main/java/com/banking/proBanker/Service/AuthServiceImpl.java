package com.banking.proBanker.Service;

import com.banking.proBanker.DTO.OtpRequest;
import com.banking.proBanker.DTO.OtpVerificationRequest;
import com.banking.proBanker.Entity.User;
import com.banking.proBanker.Repository.PasswordResetTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    @Override
    public String generatePasswordResetToken(User user) {
        return "";
    }

    @Override
    public boolean verifyPasswordResetToken(String token, User user) {
        return false;
    }

    @Override
    public void deletePasswordResetToken(String token) {

    }

    @Override
    public ResponseEntity<String> sendOtpForPasswordReset(OtpRequest otpRequest) {
        return null;
    }

    @Override
    public ResponseEntity<String> verifyOtpAndIssueResetToken(OtpVerificationRequest otpVerificationRequest) {
        return null;
    }
}
