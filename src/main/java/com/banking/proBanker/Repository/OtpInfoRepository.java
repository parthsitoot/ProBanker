package com.banking.proBanker.Repository;

import com.banking.proBanker.Entity.Account;
import com.banking.proBanker.Entity.OtpInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OtpInfoRepository extends JpaRepository<OtpInfo, Long> {
    OtpInfo findByAccountNumberAndOtp (String accountNumber, String otp);
    OtpInfo findByAccountNumber (String accountNumber);
}
