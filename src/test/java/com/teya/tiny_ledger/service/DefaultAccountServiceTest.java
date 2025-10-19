package com.teya.tiny_ledger.service;

import com.teya.tiny_ledger.dao.Account;
import com.teya.tiny_ledger.dao.Transaction;
import com.teya.tiny_ledger.dto.BalanceDto;
import com.teya.tiny_ledger.dto.TransactionDto;
import com.teya.tiny_ledger.dto.TransactionHistoryDto;
import com.teya.tiny_ledger.dto.TransactionType;
import com.teya.tiny_ledger.repository.AccountRepository;
import com.teya.tiny_ledger.repository.InMemoryAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class DefaultAccountServiceTest {


    @InjectMocks
    private DefaultAccountService accountService;

    @Mock
    private AccountRepository accountRepository;

    private UUID accountId;
    private UUID transactionId;
    private UUID reference;

    @BeforeEach
    void init() {
        accountId = UUID.randomUUID();
        transactionId = UUID.randomUUID();
        reference = UUID.randomUUID();
    }
    @Test
    void getTransactionHistory() {
        Transaction transaction = new Transaction(transactionId, TransactionType.WITHDRAWAL, BigDecimal.valueOf(10.99), "Transaction2");
        TransactionHistoryDto expected = new TransactionHistoryDto(
                List.of(
                        new TransactionDto(transactionId, null, TransactionType.WITHDRAWAL, BigDecimal.valueOf(10.99), "Transaction2")
                )
        );
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(getTestAccount(accountId, transaction)));
        TransactionHistoryDto actual = accountService.getTransactionHistory(accountId);
        verify(accountRepository).findById(accountId);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    void getTransactionHistoryThrowsAccountNotFoundException() {
        when(accountRepository.findById(accountId)).thenThrow(new AccountNotFoundException());
        assertThrows(AccountNotFoundException.class, () -> {
            accountService.getTransactionHistory(accountId);
        });

    }

    @Test
    void addTransaction() {
        Transaction transaction = new Transaction(transactionId, TransactionType.WITHDRAWAL, BigDecimal.valueOf(10.99), "Transaction2");
        TransactionDto input =new TransactionDto(transactionId, reference, TransactionType.WITHDRAWAL, BigDecimal.valueOf(10.99), "Transaction2");
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(getTestAccount(accountId, transaction)));
        TransactionDto actual = accountService.addTransaction(accountId, input);
        verify(accountRepository).findById(accountId);
        verify(accountRepository).save(any());
        assertNotNull(actual);
        assertEquals(input, actual);
    }

    @Test
    void addTransactionThrowsAccountNotFoundException() {
        TransactionDto input =new TransactionDto(transactionId, reference, TransactionType.WITHDRAWAL, BigDecimal.valueOf(10.99), "Transaction2");
        when(accountRepository.findById(accountId)).thenThrow(new AccountNotFoundException());
        assertThrows(AccountNotFoundException.class, () -> {
            accountService.addTransaction(accountId, input);
        });
    }

    @Test
    void getBalance() {
        Transaction transaction = new Transaction(transactionId, TransactionType.DEPOSIT, BigDecimal.valueOf(10.99), "Transaction2");
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(getTestAccount(accountId, transaction)));
        BalanceDto balanceDto = accountService.getBalance(accountId);
        verify(accountRepository).findById(accountId);
        assertNotNull(balanceDto);
        assertEquals(BigDecimal.valueOf(10.99), balanceDto.amount());
    }

    @Test
    void getBalanceThrowsAccountNotFoundException() {
        when(accountRepository.findById(accountId)).thenThrow(new AccountNotFoundException());
        assertThrows(AccountNotFoundException.class, () -> {
            accountService.getBalance(accountId);
        });
    }

    @Test
    void getBalanceCorrectlyCalculatesBalance() {
        Transaction transaction1 = new Transaction(transactionId, TransactionType.DEPOSIT, BigDecimal.valueOf(10.99), "Transaction2");
        Transaction transaction2 = new Transaction(transactionId, TransactionType.DEPOSIT, BigDecimal.valueOf(150.99), "Transaction2");
        Transaction transaction3 = new Transaction(transactionId, TransactionType.WITHDRAWAL, BigDecimal.valueOf(100.00), "Transaction2");
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(getTestAccount(accountId, transaction1, transaction2, transaction3)));
        BalanceDto balanceDto = accountService.getBalance(accountId);
        verify(accountRepository).findById(accountId);
        assertNotNull(balanceDto);
        assertEquals(BigDecimal.valueOf(61.98), balanceDto.amount());
    }

    private Account getTestAccount(UUID accountId, Transaction... transaction) {
        return new Account(
                accountId,
                List.of(
                        transaction
                )
        );
    }
 }