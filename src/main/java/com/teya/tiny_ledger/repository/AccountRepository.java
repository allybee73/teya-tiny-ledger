package com.teya.tiny_ledger.repository;

import com.teya.tiny_ledger.dao.Account;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepository {

    public Optional<Account> findById(UUID id);

    public Account save(Account entity);

}
