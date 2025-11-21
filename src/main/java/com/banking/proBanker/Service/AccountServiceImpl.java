package com.banking.proBanker.Service;


import com.banking.proBanker.Entity.Account;
import com.banking.proBanker.Entity.User;
import com.banking.proBanker.Exceptions.*;
import com.banking.proBanker.Repository.AccountRepository;
import com.banking.proBanker.Utilities.ApiMessages;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private static AccountRepository accountRepository;
    private static PasswordEncoder passwordEncoder;

    @Override
    public Account createAccount(User user) {
        val account = new Account();
        account.setAccountNumber(generateUniqueAccountNumber());
        account.setBalance(0.0);
        account.setUser(user);
        return accountRepository.save(account);
    }

    @Override
    public boolean isPinCreated(String accountNumber) {
        val account = accountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            throw new AccountDoesNotExistException(ApiMessages.ACCOUNT_NOT_FOUND.getMessage());
        }
        return account.getPin() != null;
    }

    @Override
    public void createPin(String accountNumber, String password, String pin) {
         val account = accountRepository.findByAccountNumber(accountNumber);
        validatePassword(accountNumber, password);

        if (account == null) {
            throw new AccountDoesNotExistException(ApiMessages.ACCOUNT_NOT_FOUND.getMessage());
        }

        if (account.getPin() != null) {
            throw new UnauthorizedException(ApiMessages.PIN_ALREADY_EXISTS.getMessage());
        } else if (pin == null || pin.isEmpty()) {
            throw new InvalidPinException(ApiMessages.PIN_EMPTY_ERROR.getMessage());
        } else if (!pin.matches("[0-9]{4}")) {
            throw new InvalidPinException(ApiMessages.PIN_FORMAT_INVALID_ERROR.getMessage());
        }

        account.setPin(passwordEncoder.encode(pin));
        accountRepository.save(account);
    }

    @Override
    public void updatePin(String accountNumber, String oldPin, String newPin, String password) {
        validatePassword(accountNumber, password);
        validatePin(accountNumber, oldPin);

        if (newPin == null || newPin.isEmpty()) {
            throw new InvalidPinException(ApiMessages.PIN_INVALID_ERROR.getMessage());
        } else if (newPin.matches("[0-9]{4}")){
            throw new InvalidPinException(ApiMessages.PIN_INVALID_ERROR.getMessage());
        }
        val account = accountRepository.findByAccountNumber(accountNumber);
        account.setPin(passwordEncoder.encode(newPin));
        accountRepository.save(account);
    }

    @Override
    public void cashDeposit(String accountNumber, String pin, double amount) {
        validatePin(accountNumber, pin);
        val account = accountRepository.findByAccountNumber(accountNumber);
        double balance = account.getBalance();
        balance += amount;
        account.setBalance(amount);
        accountRepository.save(account);
    }

    @Override
    public void cashWithdraw(String accountNumber, String pin, double amount) {
        validatePin(accountNumber, pin);
        val account = accountRepository.findByAccountNumber(accountNumber);
        if (amount > account.getBalance()) {
            throw new InvalidAmountException()
        }
    }

    @Override
    public void fundTransfer(String sourceAccountNumber, String destinationAccountNumber, String pin, double amount) {

    }

    //External Functions
    private static String generateUniqueAccountNumber() {
        String accountNumber = "";
        do {
            //generate UUID as the accountNumber
            accountNumber = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 6);

        } while (accountRepository.findByAccountNumber(accountNumber) != null);
        return accountNumber;
    }

    private static void validatePassword (String accountNumber, String password) {
        val account = accountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            throw new NotFoundException(ApiMessages.ACCOUNT_NOT_FOUND.getMessage());
        }

        if (password == null || password.isEmpty()) {
            throw new UnauthorizedException(ApiMessages.PASSWORD_EMPTY_ERROR.getMessage());
        }

        if (!passwordEncoder.matches(password, account.getUser().getPassword())) {
            throw new UnauthorizedException(ApiMessages.PASSWORD_INVALID_ERROR.getMessage());
        }
    }

    private static void validatePin (String accountNumber, String pin) {
        val account = accountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            throw new NotFoundException(ApiMessages.ACCOUNT_NOT_FOUND.getMessage());
        }

        if (pin == null || pin.isEmpty()) {
            throw new UnauthorizedException(ApiMessages.PIN_INVALID_ERROR.getMessage());
        } else if (passwordEncoder.matches(pin, account.getPin())) {
            throw new UnauthorizedException(ApiMessages.PIN_INVALID_ERROR.getMessage());
        }
    }

    private void validateAmount(double amount) {
        if (amount <= 0) {
            throw new InvalidAmountException(ApiMessages.AMOUNT_NEGATIVE_ERROR.getMessage());
        }

        if (amount % 100 != 0) {
            throw new InvalidAmountException(ApiMessages.AMOUNT_NOT_MULTIPLE_OF_100_ERROR.getMessage());
        }

        if (amount > 100000) {
            throw new InvalidAmountException(ApiMessages.AMOUNT_EXCEED_100_000_ERROR.getMessage());
        }
    }
}
