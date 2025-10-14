package com.banking.proBanker.DTO;

import com.banking.proBanker.Entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserResponse {

    private String name;
    private String email;
    private String countryCode;
    private String phoneNumber;
    private String adderss;
    private String accountNumber;
    private String ifscCode;
    private String branch;
    private String accountType;

    public UserResponse (User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.countryCode = user.getCountryCode();
        this.phoneNumber = user.getPhoneNumber();
        this.adderss = user.getAddress();
        this.accountNumber = user.getAccount().getAccountNumber();
        this.ifscCode = user.getAccount().getIfscCode();
        this.branch = user.getAccount().getBranch();
        this.accountType = user.getAccount().getAccountType();
    }
}
