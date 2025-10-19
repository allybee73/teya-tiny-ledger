package com.teya.tiny_ledger.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
@AllArgsConstructor
public class TransactionHistoryDto {
    List<TransactionDto> transactions = Collections.emptyList();
}
