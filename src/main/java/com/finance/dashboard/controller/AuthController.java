package com.finance.dashboard.controller;

import com.finance.dashboard.dto.LoginRequest;
import com.finance.dashboard.dto.RegisterRequest;
import com.finance.dashboard.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "User APIs")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(description = "Allows registered users to login.")
    public ResponseEntity<String> login(
            @Valid @RequestBody LoginRequest loginRequest
    ) {

        return ResponseEntity.status(HttpStatus.OK).body(authService.login(loginRequest));
    }
}