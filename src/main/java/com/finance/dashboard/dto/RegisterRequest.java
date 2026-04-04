package com.finance.dashboard.dto;

import org.hibernate.validator.constraints.Length;

public record RegisterRequest(String name,
                              String email,
                              @Length(min = 6, message = "Enter 6 or more characters") String password) {}
