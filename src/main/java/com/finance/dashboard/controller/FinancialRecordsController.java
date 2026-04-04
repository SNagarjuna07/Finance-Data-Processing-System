package com.finance.dashboard.controller;

import com.finance.dashboard.dto.FinancialRecordRequest;
import com.finance.dashboard.dto.FinancialRecordResponse;
import com.finance.dashboard.dto.FinancialRecordUpdateRequest;
import com.finance.dashboard.entity.User;
import com.finance.dashboard.enums.Category;
import com.finance.dashboard.enums.Type;
import com.finance.dashboard.service.FinancialRecordsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/records")
@Tag(name = "Financial Record APIs")
public class FinancialRecordsController {

    private final FinancialRecordsService financialRecordsService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(description = "Allows an admin to create a financial record.")
    public ResponseEntity<Void> create(
            @Valid @RequestBody FinancialRecordRequest financialRecordRequest
    ) {

        financialRecordsService.createTransaction(financialRecordRequest);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    @Operation(description = "Returns all the records or the records based on filtering criteria.")
    public ResponseEntity<Page<FinancialRecordResponse>> getAll(
            @RequestParam(required = false) Category category,
            @RequestParam(required = false) Type type,
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "amount") String sortBy,
            @AuthenticationPrincipal User user
    ) {

        return ResponseEntity.ok(financialRecordsService.fetchRecords(category, type, from, to, page, size, sortBy, user.getId()));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(description = "Allows an admin to update a record.")
    public ResponseEntity<Void> update(
            @PathVariable UUID id,
            @Valid @RequestBody FinancialRecordUpdateRequest request
    ) {

        financialRecordsService.updateTransaction(id, request);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(description = "Allows an admin to delete a record.")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {

        financialRecordsService.deleteTransaction(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
