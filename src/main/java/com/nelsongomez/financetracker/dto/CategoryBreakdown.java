package com.nelsongomez.financetracker.dto;

import java.math.BigDecimal;

public record CategoryBreakdown(
        String categoryName,
        BigDecimal total
) {
}
