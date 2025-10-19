package com.teya.tiny_ledger.repository;

import com.teya.tiny_ledger.dao.Account;
import com.teya.tiny_ledger.dao.Transaction;
import com.teya.tiny_ledger.dto.TransactionType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

@Component
public class InMemoryAccountRepository implements AccountRepository{

    private static Map<UUID, Account> accountMap = new HashMap<>();

    public InMemoryAccountRepository() {
        UUID account1 = UUID.randomUUID();
        UUID account2 = UUID.randomUUID();
        accountMap.put(
                account1,
                new Account(
                    account1,
                    List.of(new Transaction(UUID.randomUUID(), TransactionType.DEPOSIT, BigDecimal.valueOf(25.99), "Transaction1"),
                            new Transaction(UUID.randomUUID(), TransactionType.WITHDRAWAL, BigDecimal.valueOf(10.99), "Transaction2")
                    )
                )
        );
        accountMap.put(account2,
                new Account(
                    account2,
                    List.of(new Transaction(UUID.randomUUID(), TransactionType.DEPOSIT, BigDecimal.valueOf(250.99), "Transaction1"),
                            new Transaction(UUID.randomUUID(), TransactionType.WITHDRAWAL, BigDecimal.valueOf(100.99), "Transaction2")
                    )
                )
        );
        System.out.println("=================== ACCOUNTS ===================");

        for (Map.Entry<UUID, Account> entry: accountMap.entrySet()) {
            System.out.println("AccountId " + entry.getKey() + " Account Details: " + entry.getValue());
        }

        System.out.println("================================================");
    }

    @Override
    public Optional<Account> findById(UUID id) {

        if (accountMap.containsKey(id)) {
            return Optional.of(accountMap.get(id));
        }
        return Optional.empty();
    }

    @Override
    public Account save(Account entity) {
        accountMap.put(entity.id(), entity);
        return entity;
    }
}


