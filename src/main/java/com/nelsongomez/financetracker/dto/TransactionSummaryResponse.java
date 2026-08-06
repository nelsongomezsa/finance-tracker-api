package com.nelsongomez.financetracker.dto;

import java.math.BigDecimal;
import java.util.List;

public record TransactionSummaryResponse(
        int month,
        int year,
        BigDecimal totalIncome,
        BigDecimal totalExpense,
        BigDecimal balance,
        List<CategoryBreakdown> byCategory
) {
}
