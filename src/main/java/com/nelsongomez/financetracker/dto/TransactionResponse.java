package com.nelsongomez.financetracker.dto;

import com.nelsongomez.financetracker.model.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionResponse(
        Long id,
        String description,
        BigDecimal amount,
        LocalDate date,
        CategoryResponse category
) {

    public static TransactionResponse fromEntity(Transaction transaction) {
        return new TransactionResponse(
                transaction.getId(),
                transaction.getDescription(),
                transaction.getAmount(),
                transaction.getDate(),
                CategoryResponse.fromEntity(transaction.getCategory())
        );
    }
}
