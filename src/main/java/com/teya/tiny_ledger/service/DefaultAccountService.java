package com.teya.tiny_ledger.service;

import com.teya.tiny_ledger.dao.Account;
import com.teya.tiny_ledger.dto.*;
import com.teya.tiny_ledger.repository.AccountRepository;
import com.teya.tiny_ledger.dao.Transaction;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
public class DefaultAccountService implements AccountService {

    AccountRepository accountRepository;

    public DefaultAccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public TransactionHistoryDto getTransactionHistory(UUID accountId) {
        return accountRepository.findById(accountId)
                .map(account -> new TransactionHistoryDto(
                        account.transactionList().stream()
                                .map(this::mapToTransactionResponse)
                                .toList()
                ))
                .orElseThrow(AccountNotFoundException::new);
    }


    @Override
    public TransactionDto addTransaction(UUID accountId, TransactionDto transactionDto) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException());

        UUID transactionId = UUID.randomUUID();
        List<Transaction> updatedList = new ArrayList<>(account.transactionList());
        updatedList.add(mapToTransaction(transactionId, transactionDto));

        Account updatedAccount = new Account(
                account.id(),
                updatedList
        );

        accountRepository.save(updatedAccount);

        return transactionDto;
    }


    @Override
    public BalanceDto getBalance(UUID accountId) {
        return accountRepository.findById(accountId).map(
                account ->
                        new BalanceDto(
                                account.transactionList().stream().map(
                                        transaction ->
                                                transaction.type().equals(TransactionType.WITHDRAWAL) ? transaction.amount().negate() : transaction.amount()
                                ).reduce(
                                        BigDecimal.ZERO, (a, b) -> a.add(b)
                                ).setScale(2, RoundingMode.HALF_UP)
                        )
        ).orElseThrow(AccountNotFoundException::new);

    }

    private TransactionDto mapToTransactionResponse(Transaction transaction) {
        return new TransactionDto(
                transaction.id(),
                null,
                transaction.type(),
                transaction.amount().setScale(2, RoundingMode.HALF_UP),
                transaction.description()
        );
    }

    private Transaction mapToTransaction(UUID transactionId, TransactionDto transactionDto) {
        return new Transaction(
                transactionId,
                transactionDto.type(),
                transactionDto.amount(),
                transactionDto.description()
        );
    }
}
