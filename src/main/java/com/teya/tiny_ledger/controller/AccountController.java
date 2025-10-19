package com.teya.tiny_ledger.controller;

import com.teya.tiny_ledger.dto.BalanceDto;
import com.teya.tiny_ledger.dto.ErrorResponseDto;
import com.teya.tiny_ledger.dto.TransactionDto;
import com.teya.tiny_ledger.dto.TransactionHistoryDto;
import com.teya.tiny_ledger.service.AccountNotFoundException;
import com.teya.tiny_ledger.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/{accountId}/balance")
    public BalanceDto getBalance(
            @PathVariable UUID accountId
    ) {
        return accountService.getBalance(accountId);
    }

    @PostMapping("/{accountId}/transactions")
    public TransactionDto createTransaction(
            @PathVariable UUID accountId,
            @Valid @RequestBody TransactionDto request
    ) {
        return accountService.addTransaction(accountId, request);
    }

    @GetMapping("/{accountId}/transactions")
    public TransactionHistoryDto getTransactions(
            @PathVariable UUID accountId
    ) {
        return accountService.getTransactionHistory(accountId);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(AccountNotFoundException.class)
    public ErrorResponseDto handleAccountNotFoundException() {
        return new ErrorResponseDto("Account not found");
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponseDto handleException() {
        return new ErrorResponseDto("Error executing request");
    }

}