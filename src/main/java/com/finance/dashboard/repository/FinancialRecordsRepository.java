package com.finance.dashboard.repository;

import com.finance.dashboard.entity.FinancialRecord;
import com.finance.dashboard.entity.User;
import com.finance.dashboard.enums.Category;
import com.finance.dashboard.enums.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface FinancialRecordsRepository extends JpaRepository<FinancialRecord, UUID> {

    Page<FinancialRecord> findByCreatedBy(User user, Pageable pageable);

    // Total by type (INCOME or EXPENSE)
    @Query("SELECT COALESCE(SUM(f.amount), 0) FROM FinancialRecord f WHERE f.type = :type AND f.deletedAt IS NULL")
    BigDecimal sumByType(@Param("type") Type type);

    // Totals grouped by category
    @Query("SELECT f.category, SUM(f.amount) FROM FinancialRecord f WHERE f.deletedAt IS NULL GROUP BY f.category")
    List<Object[]> sumByCategory();

    // Monthly trends
    @Query("SELECT MONTH(f.date), YEAR(f.date), f.type, SUM(f.amount) FROM FinancialRecord f WHERE f.deletedAt IS NULL GROUP BY YEAR(f.date), MONTH(f.date), f.type ORDER BY YEAR(f.date), MONTH(f.date)")
    List<Object[]> monthlyTrends();

    List<FinancialRecord> findTop5ByDeletedAtIsNullOrderByCreatedAtDesc();

    @Query("SELECT f FROM FinancialRecord f WHERE " +
            "(:category IS NULL OR f.category = :category) AND " +
            "(:type IS NULL OR f.type = :type) AND " +
            "(:from IS NULL OR f.date >= :from) AND " +
            "(:to IS NULL OR f.date <= :to) AND " +
            "f.createdBy.id = :userId AND " +
            "f.deletedAt IS NULL")
    Page<FinancialRecord> findWithFilters(
            @Param("category") Category category,
            @Param("type") Type type,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to,
            @Param("userId") UUID userId,
            Pageable pageable);
}
