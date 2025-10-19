package com.teya.tiny_ledger.service;

import com.teya.tiny_ledger.dto.BalanceDto;
import com.teya.tiny_ledger.dto.TransactionDto;
import com.teya.tiny_ledger.dto.TransactionHistoryDto;

import java.util.UUID;

public interface AccountService {

    public TransactionHistoryDto getTransactionHistory(UUID accountId);

    public TransactionDto addTransaction(UUID accountId, TransactionDto transactionDto);

    public BalanceDto getBalance(UUID accountId);
}
