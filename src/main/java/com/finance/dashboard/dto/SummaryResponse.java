package com.finance.dashboard.dto;

import java.math.BigDecimal;

public record SummaryResponse(BigDecimal totalIncome,
                              BigDecimal totalExpenses,
                              BigDecimal netBalance) {
}
