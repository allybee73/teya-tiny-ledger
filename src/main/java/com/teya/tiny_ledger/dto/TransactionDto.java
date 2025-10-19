package com.teya.tiny_ledger.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record TransactionDto(
        UUID id,
        UUID reference,
        TransactionType type,
        BigDecimal amount,
        String description
) {
}
