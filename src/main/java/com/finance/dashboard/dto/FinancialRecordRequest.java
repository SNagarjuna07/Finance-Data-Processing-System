package com.finance.dashboard.dto;

import com.finance.dashboard.enums.Category;
import com.finance.dashboard.enums.Type;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PastOrPresent;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record FinancialRecordRequest(UUID userId,
                                     @Min(value = 1, message = "Amount should be in positive")
                                     BigDecimal amount,
                                     Type type,
                                     Category category,
                                     String notes,
                                     @PastOrPresent(message = "Transaction date cannot be in the future")
                                     LocalDate date) {
}
