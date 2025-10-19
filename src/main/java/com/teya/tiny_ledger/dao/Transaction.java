package com.teya.tiny_ledger.dao;

import com.teya.tiny_ledger.dto.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;


public record Transaction (
    UUID id,
    TransactionType type,
    BigDecimal amount,
    String description
){}
