package com.finance.dashboard.service;

import com.finance.dashboard.dto.FinancialRecordRequest;
import com.finance.dashboard.dto.FinancialRecordResponse;
import com.finance.dashboard.dto.FinancialRecordUpdateRequest;
import com.finance.dashboard.entity.FinancialRecord;
import com.finance.dashboard.entity.User;
import com.finance.dashboard.enums.Category;
import com.finance.dashboard.enums.Type;
import com.finance.dashboard.excpetion.ResourceNotFoundException;
import com.finance.dashboard.excpetion.UserNotFoundException;
import com.finance.dashboard.repository.FinancialRecordsRepository;
import com.finance.dashboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FinancialRecordsService {

    private final FinancialRecordsRepository financialRecordsRepository;

    private final UserRepository userRepository;

    @Transactional
    public void createTransaction(FinancialRecordRequest request) {

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        FinancialRecord record = new FinancialRecord();

        record.setAmount(request.amount());
        record.setType(request.type());
        record.setCategory(request.category());
        record.setNotes(request.notes());
        record.setDate(request.date());
        record.setCreatedBy(user);
        record.setCreatedAt(LocalDateTime.now());

        financialRecordsRepository.save(record);
    }

    @Transactional(readOnly = true)
    public Page<FinancialRecordResponse> fetchRecords(
            Category category,
            Type type,
            LocalDate from,
            LocalDate to,
            int page,
            int size,
            String sortBy,
            UUID userId) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());

        return financialRecordsRepository.findWithFilters(category, type, from, to, userId, pageable)
                .map(this::toResponse);
    }

    @Transactional
    public void updateTransaction(UUID id, FinancialRecordUpdateRequest request) {

        FinancialRecord record = financialRecordsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Record not found"));

        record.setAmount(request.amount());
        record.setType(request.type());
        record.setCategory(request.category());
        record.setNotes(request.notes());
        record.setDate(request.date());

        financialRecordsRepository.save(record);
    }

    @Transactional
    public void deleteTransaction(UUID id) {

        FinancialRecord record = financialRecordsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Record not found"));

        record.setDeletedAt(LocalDateTime.now());

        //financialRecordsRepository.deleteById(record.getId());
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
