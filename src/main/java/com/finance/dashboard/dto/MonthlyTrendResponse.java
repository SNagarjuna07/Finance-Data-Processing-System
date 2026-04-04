package com.finance.dashboard.dto;

import java.math.BigDecimal;

public record MonthlyTrendResponse(int year,
                                   int month,
                                   BigDecimal totalIncome,
                                   BigDecimal totalExpenses) {
}
