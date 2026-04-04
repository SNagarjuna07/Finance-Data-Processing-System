package com.finance.dashboard.dto;

import com.finance.dashboard.enums.Role;
import com.finance.dashboard.enums.Status;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponse(UUID id,
                           String name,
                           String email,
                           Role role,
                           Status status,
                           LocalDateTime createdAt) {
}
