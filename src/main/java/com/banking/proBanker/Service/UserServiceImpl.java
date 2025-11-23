package com.banking.proBanker.Service;

import com.banking.proBanker.DTO.LoginRequest;
import com.banking.proBanker.DTO.OtpRequest;
import com.banking.proBanker.DTO.OtpVerificationRequest;
import com.banking.proBanker.Entity.User;
import com.banking.proBanker.Exceptions.InvalidTokenException;
import com.banking.proBanker.Repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private static UserRepository userRepository;

    @Override
    public ResponseEntity<String> registerUser(User user) {
        return null;
    }

    @Override
    public ResponseEntity<String> login(LoginRequest loginRequest, HttpServletRequest request) throws InvalidTokenException {
        return null;
    }

    @Override
    public ResponseEntity<String> generateOtp(OtpRequest otpRequest) {
        return null;
    }

    @Override
    public ResponseEntity<String> verifyOtpAndLogin(OtpVerificationRequest otpVerificationRequest) throws InvalidTokenException {
        return null;
    }

    @Override
    public ResponseEntity<String> updateUser(User user) {
        return null;
    }

    @Override
    public ModelAndView logout(String token) throws InvalidTokenException {
        return null;
    }

    @Override
    public boolean resetPassword(User user, String newPassword) {
        return false;
    }

    @Override
    public User saveUser(User user) {
        return null;
    }

    @Override
    public User getUserByIdentifier(String identifier) {
        return null;
    }

    @Override
    public User getUserByAccountNumber(String accountNo) {
        return null;
    }

    @Override
    public User getUserByEmail(String email) {
        return null;
    }
}
