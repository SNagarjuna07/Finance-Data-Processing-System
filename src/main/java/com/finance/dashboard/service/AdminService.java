package com.finance.dashboard.service;

import com.finance.dashboard.dto.RegisterRequest;
import com.finance.dashboard.dto.UpdateRoleRequest;
import com.finance.dashboard.dto.UpdateStatusRequest;
import com.finance.dashboard.dto.UserResponse;
import com.finance.dashboard.entity.User;
import com.finance.dashboard.enums.Role;
import com.finance.dashboard.enums.Status;
import com.finance.dashboard.excpetion.DuplicateEmailException;
import com.finance.dashboard.excpetion.UserNotFoundException;
import com.finance.dashboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void register(RegisterRequest registerRequest) {

        userRepository.findByEmail(registerRequest.email())
                .ifPresent(user ->
                        {
                            throw new DuplicateEmailException("An account with this email already exists");
                        }
                );

        User user = new User();

        user.setName(registerRequest.name());
        user.setEmail(registerRequest.email());
        user.setPassword(passwordEncoder.encode(registerRequest.password()));
        user.setRole(Role.VIEWER);
        user.setStatus(Status.ACTIVE);
        user.setCreatedAt(LocalDateTime.now());

        userRepository.save(user);
    }

    @Transactional
    public void registerAdmin(RegisterRequest registerRequest) {

        userRepository.findByEmail(registerRequest.email())
                .ifPresent(user ->
                        {
                            throw new DuplicateEmailException("An account with this email already exists");
                        }
                );

        User user = new User();

        user.setName(registerRequest.name());
        user.setEmail(registerRequest.email());
        user.setPassword(passwordEncoder.encode(registerRequest.password()));
        user.setRole(Role.ADMIN);
        user.setStatus(Status.ACTIVE);
        user.setCreatedAt(LocalDateTime.now());

        userRepository.save(user);
    }

    @Transactional
    public void registerAnalyst(RegisterRequest registerRequest) {

        userRepository.findByEmail(registerRequest.email())
                .ifPresent(user ->
                        {
                            throw new DuplicateEmailException("An account with this email already exists");
                        }
                );

        User user = new User();

        user.setName(registerRequest.name());
        user.setEmail(registerRequest.email());
        user.setPassword(passwordEncoder.encode(registerRequest.password()));
        user.setRole(Role.ANALYST);
        user.setStatus(Status.ACTIVE);
        user.setCreatedAt(LocalDateTime.now());

        userRepository.save(user);
    }

    @Transactional
    public void updateRole(UUID userId, UpdateRoleRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        user.setRole(request.role());

        userRepository.save(user);

    }

    @Transactional
    public void updateStatus(UUID userId, UpdateStatusRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        user.setStatus(request.status());

        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public Page<UserResponse> getAllUsers(int page, int size, String sortBy) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());

        return userRepository.findAll(pageable)
                .map(this::toResponse);
    }

    private UserResponse toResponse(User user) {

        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getStatus(),
                user.getCreatedAt()
        );
    }

}
