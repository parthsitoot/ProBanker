package com.banking.proBanker.Service;

import com.banking.proBanker.DTO.LoginRequest;
import com.banking.proBanker.DTO.OtpRequest;
import com.banking.proBanker.DTO.OtpVerificationRequest;
import com.banking.proBanker.Entity.User;
import com.banking.proBanker.Exceptions.InvalidTokenException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.ModelAndView;

public interface UserService {

    public ResponseEntity<String> registerUser(User user);

    public ResponseEntity<String> login(LoginRequest loginRequest, HttpServletRequest request)
            throws InvalidTokenException;

    public ResponseEntity<String> generateOtp(OtpRequest otpRequest);

    public ResponseEntity<String> verifyOtpAndLogin(OtpVerificationRequest otpVerificationRequest)
            throws InvalidTokenException;

    public ResponseEntity<String> updateUser(User user);

    public ModelAndView logout(String token) throws InvalidTokenException;

    public boolean resetPassword(User user, String newPassword);

    public User saveUser(User user);

    public User getUserByIdentifier(String identifier);

    public User getUserByAccountNumber(String accountNo);

    public User getUserByEmail(String email);

}
