package com.banking.proBanker.Service;

import com.banking.proBanker.Entity.Account;
import com.banking.proBanker.Entity.User;

public interface AccountService {

    public Account createAccount (User user);
    public boolean isPinCreated (String accountNumber);
    public void createPin (String accountNumber, String password, String pin);
    public void updatePin (String accountNumber, String oldPin, String newPin, String password);
    public void cashDeposit (String accountNumber, String pin, double amount);
    public void cashWithdraw (String accountNumber, String pin, double amount);
    public void fundTransfer (String sourceAccountNumber, String destinationAccountNumber, String pin, double amount);
}
