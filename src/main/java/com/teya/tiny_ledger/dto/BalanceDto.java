package com.teya.tiny_ledger.dto;

import java.math.BigDecimal;

public record BalanceDto(
        BigDecimal amount
) {}
