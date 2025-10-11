package com.banking.proBanker.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class AccountResponse {
    private String accountNumber;
    private double balance;
    private String accountType;
    private String branch;
    private String ifscCode;

    public AccountResponse(String accountNumber, double balance, String accountType, String branch, String ifscCode) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.accountType = accountType;
        this.branch = branch;
        this.ifscCode = ifscCode;
    }
}
