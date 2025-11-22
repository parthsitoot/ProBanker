package com.banking.proBanker.Service;

import com.banking.proBanker.DTO.AccountResponse;
import com.banking.proBanker.DTO.UserResponse;

public interface DashboardService {
    public UserResponse getUserDetails(String accoutNumber);
    public AccountResponse getAccountDetails(String accountNumber);
}
