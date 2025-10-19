package com.teya.tiny_ledger.dao;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;


public record Account(
    UUID id,
    List<Transaction> transactionList
){ }
