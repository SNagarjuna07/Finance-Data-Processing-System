package com.finance.dashboard.controller;

import com.finance.dashboard.dto.CategorySummaryResponse;
import com.finance.dashboard.dto.FinancialRecordResponse;
import com.finance.dashboard.dto.MonthlyTrendResponse;
import com.finance.dashboard.dto.SummaryResponse;
import com.finance.dashboard.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ANALYST', 'ADMIN')")
@Tag(name = "Dashboard APIs")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/summary")
    @Operation(description = "Returns the total income and expense and current balance  .")
    public ResponseEntity<SummaryResponse> getSummary() {
        return ResponseEntity.ok(dashboardService.getSummary());
    }

    @GetMapping("/by-category")
    @Operation(description = "Returns the sum of all income and expenses in each category.")
    public ResponseEntity<List<CategorySummaryResponse>> getByCategory() {
        return ResponseEntity.ok(dashboardService.getCategoryBreakdown());
    }

    @GetMapping("/trends")
    @Operation(description = "Monthly total of income and expense.")
    public ResponseEntity<List<MonthlyTrendResponse>> getTrends() {
        return ResponseEntity.ok(dashboardService.getMonthlyTrends());
    }

    @GetMapping("/recent")
    @Operation(description = "Returns the top 5 financial records.")
    public ResponseEntity<List<FinancialRecordResponse>> getRecent() {
        return ResponseEntity.ok(dashboardService.getRecentActivity());
    }
}