package com.banking.proBanker.Controller;


import com.banking.proBanker.DTO.AmountRequest;
import com.banking.proBanker.DTO.FundTransferResponse;
import com.banking.proBanker.DTO.PinRequest;
import com.banking.proBanker.Service.AccountService;
import com.banking.proBanker.Service.TransactionService;
import com.banking.proBanker.Utilities.ApiMessages;
import com.banking.proBanker.Utilities.JsonUtil;
import com.banking.proBanker.Utilities.LoggedinUser;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class AccountController {
    private AccountService accountService;
    private TransactionService transactionService;

    @GetMapping("/pin/check")
    public ResponseEntity<String> checkAccountPin () {
        val isPinValid = accountService.isPinCreated(LoggedinUser.getAccountNumber());
        val response = isPinValid ? ApiMessages.PIN_CREATED.getMessage()
                : ApiMessages.PIN_NOT_CREATED.getMessage();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/pin/create")
    public ResponseEntity<String> createPin (@RequestBody PinRequest pinRequest) {
        accountService.createPin(
                LoggedinUser.getAccountNumber(),
                pinRequest.password(),
                pinRequest.pin()
        );
        return ResponseEntity.ok(ApiMessages.PIN_CREATION_SUCCESS.getMessage());
    }

    @PostMapping("/deposit")
    public ResponseEntity<String> cashDeposit (@RequestBody AmountRequest amountRequest) {
        accountService.cashDeposit(
                LoggedinUser.getAccountNumber(),
                amountRequest.pin(),
                amountRequest.amount()
        );
        return ResponseEntity.ok(ApiMessages.CASH_DEPOSIT_SUCCESS.getMessage());
    }

    @PostMapping("/withdraw")
    public ResponseEntity<String> cashWithdraw (@RequestBody AmountRequest amountRequest) {
        accountService.cashWithdraw(
                LoggedinUser.getAccountNumber(),
                amountRequest.pin(),
                amountRequest.amount()
        );
        return ResponseEntity.ok(ApiMessages.CASH_WITHDRAWAL_SUCCESS.getMessage());
    }

    @PostMapping("/fundTransfer")
    public ResponseEntity<String> fundTransfer (@RequestBody FundTransferResponse fundTransferResponse) {
        accountService.fundTransfer(
                LoggedinUser.getAccountNumber(),
                fundTransferResponse.targetAccountNumber(),
                fundTransferResponse.pin(),
                fundTransferResponse.amount()
        );
        return ResponseEntity.ok(ApiMessages.CASH_TRANSFER_SUCCESS.getMessage());
    }

    @GetMapping("/statement")
    public ResponseEntity<String> getAllTransactions () {
        val transactions = transactionService.
                getAllTransactionsByAccountNumber(
                        LoggedinUser.getAccountNumber()
                );

        return ResponseEntity.ok(JsonUtil.toJSON(transactions));
    }
}
