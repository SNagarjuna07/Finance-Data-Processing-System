package com.finance.dashboard.service;

import com.finance.dashboard.dto.CategorySummaryResponse;
import com.finance.dashboard.dto.FinancialRecordResponse;
import com.finance.dashboard.dto.MonthlyTrendResponse;
import com.finance.dashboard.dto.SummaryResponse;
import com.finance.dashboard.entity.FinancialRecord;
import com.finance.dashboard.enums.Type;
import com.finance.dashboard.repository.FinancialRecordsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final FinancialRecordsRepository recordRepository;

    public SummaryResponse getSummary() {
        BigDecimal income = recordRepository.sumByType(Type.INCOME);
        BigDecimal expenses = recordRepository.sumByType(Type.EXPENSE);
        return new SummaryResponse(income, expenses, income.subtract(expenses));
    }

    public List<CategorySummaryResponse> getCategoryBreakdown() {
        return recordRepository.sumByCategory()
                .stream()
                .map(row -> new CategorySummaryResponse(row[0].toString(), (BigDecimal) row[1]))
                .toList();
    }

    public List<MonthlyTrendResponse> getMonthlyTrends() {
        Map<String, MonthlyTrendResponse> map = new LinkedHashMap<>();

        recordRepository.monthlyTrends().forEach(row -> {
            int month = (int) row[0];
            int year = (int) row[1];
            Type type = (Type) row[2];
            BigDecimal total = (BigDecimal) row[3];
            String key = year + "-" + month;

            MonthlyTrendResponse prev = map.getOrDefault(key, new MonthlyTrendResponse(year, month, BigDecimal.ZERO, BigDecimal.ZERO));
            map.put(key, type == Type.INCOME
                    ? new MonthlyTrendResponse(year, month, total, prev.totalExpenses())
                    : new MonthlyTrendResponse(year, month, prev.totalIncome(), total));
        });

        return new ArrayList<>(map.values());
    }

    public List<FinancialRecordResponse> getRecentActivity() {
        return recordRepository.findTop5ByDeletedAtIsNullOrderByCreatedAtDesc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private FinancialRecordResponse toResponse(FinancialRecord record) {

        return new FinancialRecordResponse(
                record.getAmount(),
                record.getType(),
                record.getCategory(),
                record.getNotes(),
                record.getCreatedBy().getName(),
                record.getDate()
        );
    }
}