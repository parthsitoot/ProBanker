package com.banking.proBanker.Controller;

import com.banking.proBanker.DTO.OtpRequest;
import com.banking.proBanker.DTO.OtpVerificationRequest;
import com.banking.proBanker.DTO.ResetPasswordRequest;
import com.banking.proBanker.Service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/authentication")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/password-reset/send-otp")
    public ResponseEntity<String> sendOtpForPasswordReset (@RequestBody OtpRequest otpRequest) {
        return authService.sendOtpForPasswordReset(otpRequest);
    }

    @PostMapping ("/password-reset/verify-otp")
    public ResponseEntity<String> verifyOtpAndIssueResetToken (@RequestBody OtpVerificationRequest otpVerificationRequest) {
        return authService.verifyOtpAndIssueResetToken(otpVerificationRequest);
    }

    @PostMapping ("/password-reset")
    public ResponseEntity<String> resetPassword (@RequestBody ResetPasswordRequest resetPasswordRequest) {
        return authService.resetPassword(resetPasswordRequest);
    }

}
