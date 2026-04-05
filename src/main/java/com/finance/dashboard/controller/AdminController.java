package com.finance.dashboard.controller;

import com.finance.dashboard.dto.RegisterRequest;
import com.finance.dashboard.dto.UpdateRoleRequest;
import com.finance.dashboard.dto.UpdateStatusRequest;
import com.finance.dashboard.dto.UserResponse;
import com.finance.dashboard.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin APIs")
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/create-user")
    @Operation(description = "Allows an Admin to create a user.")
    public ResponseEntity<String> registerUser(
            @Valid @RequestBody RegisterRequest registerRequest
    ) {

        adminService.register(registerRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Registered successfully.");
    }

    @Operation(description = "Allows an admin to create a user as Admin.")
    @PostMapping("/create-admin")
    public ResponseEntity<String> registerAdmin(
            @Valid @RequestBody RegisterRequest registerRequest
    ) {

        adminService.registerAdmin(registerRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Registered successfully. Now you can login.");
    }

    @PostMapping("/create-analyst")
    @Operation(description = "Allows an admin to create a user as Analyst.")
    public ResponseEntity<String> registerAnalyst(
            @Valid @RequestBody RegisterRequest registerRequest
    ) {

        adminService.registerAnalyst(registerRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Registered successfully. Now you can login.");
    }

    @PatchMapping("/{id}/role")
    @Operation(description = "Allows an admin to update the role of a user.")
    public ResponseEntity<Void> updateRole(
            @PathVariable UUID id,
            @RequestBody UpdateRoleRequest request
    ) {

        adminService.updateRole(id, request);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/{id}/status")
    @Operation(description = "Allows an admin to update the status of a user's profile.")
    public ResponseEntity<Void> updateStatus(
            @PathVariable UUID id,
            @RequestBody UpdateStatusRequest request
    ) {

        adminService.updateStatus(id, request);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/users")
    @Operation(description = "Returns all the users.")
    public ResponseEntity<Page<UserResponse>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy) {

        return ResponseEntity.status(HttpStatus.OK).body(adminService.getAllUsers(page, size, sortBy));
    }
}