package com.banking.proBanker.Service;

import com.banking.proBanker.DTO.LoginRequest;
import com.banking.proBanker.DTO.OtpRequest;
import com.banking.proBanker.DTO.OtpVerificationRequest;
import com.banking.proBanker.Entity.User;
import com.banking.proBanker.Exceptions.InvalidOtpException;
import com.banking.proBanker.Exceptions.InvalidTokenException;
import com.banking.proBanker.Exceptions.UserInvalidException;
import com.banking.proBanker.Mapper.UserMapper;
import com.banking.proBanker.Repository.UserRepository;
import com.banking.proBanker.Utilities.ApiMessages;
import com.banking.proBanker.Utilities.ValidateUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Timestamp;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final AccountService accountService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final GeolocationService geolocationService;
    private final OtpService otpService;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final UserDetailsService userDetailsService;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final ValidateUtil validationUtil;


    @Override
    public ResponseEntity<String> registerUser(User user) {
        validationUtil.validateNewUser(user);

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
        return userRepository.save(user);
    }

    @Override
    public User getUserByIdentifier(String identifier) {
        User user = null;

        if (validationUtil.doesEmailExist(identifier)) {
            user = getUserByEmail(identifier);
        } else if (validationUtil.doesAccountExist(identifier)) {
            user = getUserByAccountNumber(identifier);
        } else {
            throw new UserInvalidException(
                    String.format(ApiMessages.USER_NOT_FOUND_BY_IDENTIFIER.getMessage(), identifier));
        }

        return user;
    }

    @Override
    public User getUserByAccountNumber(String accountNo) {
        return null;
    }

    @Override
    public User getUserByEmail(String email) {
        return null;
    }

    //Additional functions

    private void encodePassword (User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCountryCode(user.getCountryCode().toUpperCase());
    }

    private User saveUserWithAccount (User user) {
        val savedUser = saveUser(user);
        savedUser.setAccount(accountService.createAccount(user));
        return saveUser(user);
    }

    private User authenticateUser (LoginRequest loginRequest) {
        val user = getUserByIdentifier(loginRequest.identifier());
        val accountNumber = user.getAccount().getAccountNumber();
        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(accountNumber, loginRequest.password())
        );
        return user;
    }

    private void authenticateUser (String accountNumber, String password) {
        authenticationManager
                .authenticate(
                        new UsernamePasswordAuthenticationToken(accountNumber, password)
                );
    }

    private String generateAndSaveToken (String accountNumber) throws InvalidTokenException {
        val userDetails = userDetailsService.loadUserByUsername(accountNumber);
        val token = tokenService.generateToken(userDetails);
        tokenService.saveToken(token);
        return token;
    }

    private ResponseEntity<String> sendOtpEmail(User user, String otp) {
        val emailSendingFuture = otpService.sendOTPByEmail(
                user.getEmail(), user.getName(), user.getAccount().getAccountNumber(), otp);

        ResponseEntity<String> successResponse = ResponseEntity
                .ok(String.format(ApiMessages.OTP_SENT_SUCCESS.getMessage(), user.getEmail()));
        ResponseEntity<String> failureResponse = ResponseEntity.internalServerError()
                .body(String.format(ApiMessages.OTP_SENT_FAILURE.getMessage(), user.getEmail()));

        return emailSendingFuture.thenApply(result -> successResponse)
                .exceptionally(e -> failureResponse).join();
    }

    private void validateOtpRequest (OtpVerificationRequest request) {
        if (request.identifier() == null || request.identifier().isEmpty()) {
            throw new IllegalArgumentException(ApiMessages.IDENTIFIER_MISSING_ERROR.getMessage());
        }
        if (request.otp() == null || request.otp().isEmpty()) {
            throw new IllegalArgumentException(ApiMessages.OTP_INVALID_ERROR.getMessage());
        }
    }

    private void validateOtp (User user, String otp) {
        if (!otpService.validateOTP(user.getAccount().getAccountNumber(), otp)) {
            throw new InvalidOtpException(ApiMessages.OTP_INVALID_ERROR.getMessage());
        }

    }

    private void updateuser (User existingUser, User newUser) {
        validationUtil.validateUserDetails(newUser);

        newUser.setPassword(existingUser.getPassword());
        userMapper.userUpdate(existingUser, newUser);
    }

    private CompletableFuture<Boolean> setLoginNotification (User user, String ip) {
        val loginTime = new Timestamp(System.currentTimeMillis()).toString();

        return geolocationService.getGeolocation(ip)
                .thenComposeAsync(geolocationResponse -> {
                    val loginLocation = String.format("%s %s",
                            geolocationResponse.getCity().getNames().get("en"),
                            geolocationResponse.getCountry().getNames().get("en"));
                    return sendLoginEmail(user, loginTime, loginLocation);
                });
    }

    private CompletableFuture<Boolean> sendLoginEmail (User user, String loginTime, String loginLocation) {
        val emailText = emailService.getLoginEmailTemplate(user.getName(), loginTime, loginLocation);
        return emailService.sendEmail(user.getEmail(), ApiMessages.EMAIL_SUBJECT_LOGIN.getMessage(), emailText)
                .thenApplyAsync(result -> true)
                .exceptionally(ex -> false);
    }
}
