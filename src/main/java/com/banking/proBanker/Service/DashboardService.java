package com.banking.proBanker.Service;

import com.banking.proBanker.DTO.AccountResponse;
import com.banking.proBanker.DTO.UserResponse;

public interface DashboardService {
    UserResponse getUserDetails(String accoutNumber);
    AccountResponse getAccountDetails(String accountNumber);
}
