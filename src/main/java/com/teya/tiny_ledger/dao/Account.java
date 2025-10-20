package com.teya.tiny_ledger.dao;

import java.util.List;
import java.util.UUID;


public record Account(
        UUID id,
        List<Transaction> transactionList
) {
}
