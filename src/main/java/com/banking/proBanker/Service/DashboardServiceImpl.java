package com.banking.proBanker.Service;

import com.banking.proBanker.DTO.AccountResponse;
import com.banking.proBanker.DTO.UserResponse;
import com.banking.proBanker.Exceptions.AccountDoesNotExistException;
import com.banking.proBanker.Exceptions.NotFoundException;
import com.banking.proBanker.Repository.AccountRepository;
import com.banking.proBanker.Repository.UserRepository;
import com.banking.proBanker.Utilities.ApiMessages;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private static UserRepository userRepository;
    private static AccountRepository accountRepository;

    @Override
    public UserResponse getUserDetails(String accoutNumber) {

        val user = userRepository.findByAccountAccountNumber(accoutNumber)
                .orElseThrow(() -> new NotFoundException(String.format(
                        ApiMessages.USER_NOT_FOUND_BY_ACCOUNT.getMessage(), accoutNumber
                )));
        return new UserResponse(user);
    }

    @Override
    public AccountResponse getAccountDetails(String accountNumber) {
        val account = accountRepository.findByAccountNumber(accountNumber);

        if (account == null) {
            throw new AccountDoesNotExistException(ApiMessages.ACCOUNT_NOT_FOUND.getMessage());
        }

        return new AccountResponse(account);
    }

}
