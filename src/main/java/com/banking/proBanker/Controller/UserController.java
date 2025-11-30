package com.banking.proBanker.Controller;

import com.banking.proBanker.DTO.LoginRequest;
import com.banking.proBanker.DTO.OtpRequest;
import com.banking.proBanker.DTO.OtpVerificationRequest;
import com.banking.proBanker.Entity.User;
import com.banking.proBanker.Exceptions.InvalidTokenException;
import com.banking.proBanker.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequiredArgsConstructor
@RequestMapping ("/api/user")
public class UserController {
    private final UserService userService;

    @PostMapping ("/register")
    public ResponseEntity<String> registerUser (@RequestBody User user) {
        return userService.registerUser(user);
    }

    @PostMapping ("/login")
    public ResponseEntity<String> login (@RequestBody LoginRequest loginRequest, HttpServletRequest request)
        throws InvalidTokenException {
        return userService.login(loginRequest, request);
    }

    @PostMapping ("/generate-otp")
    public ResponseEntity<String> generateOtp (@RequestBody OtpRequest otpRequest)
        throws InvalidTokenException {
        return userService.generateOtp(otpRequest);
    }

    @PostMapping ("/verify-otp")
    public ResponseEntity<String> verifyOtp (@RequestBody OtpVerificationRequest otpVerificationRequest)
        throws InvalidTokenException {
        return userService.verifyOtpAndLogin(otpVerificationRequest);
    }

    @PostMapping ("/update")
    public ResponseEntity<String> updateUser (@RequestBody User user) {
        return userService.updateUser(user);
    }

    @GetMapping ("/logout")
    public ModelAndView logout (@RequestHeader ("Authorization") String token)
        throws InvalidTokenException {
        return userService.logout(token);
    }
}
