package com.finance.dashboard.dto;

import com.finance.dashboard.enums.Category;
import com.finance.dashboard.enums.Type;

import java.math.BigDecimal;
import java.time.LocalDate;

public record FinancialRecordResponse(BigDecimal amount,
                                      Type type,
                                      Category category,
                                      String notes,
                                      String createdBy,
                                      LocalDate date) {
}
