package com.finance.dashboard.service;

import com.finance.dashboard.dto.LoginRequest;
import com.finance.dashboard.dto.RegisterRequest;
import com.finance.dashboard.entity.User;
import com.finance.dashboard.enums.Role;
import com.finance.dashboard.enums.Status;
import com.finance.dashboard.excpetion.DuplicateEmailException;
import com.finance.dashboard.excpetion.InactiveUserException;
import com.finance.dashboard.excpetion.ResourceNotFoundException;
import com.finance.dashboard.excpetion.UserNotFoundException;
import com.finance.dashboard.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final CustomUserDetailsService userDetailsService;

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

    public String login(LoginRequest loginRequest) {

        User user = userRepository.findByEmail(loginRequest.email())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (user.getStatus() == Status.INACTIVE) {
            throw new InactiveUserException("Your account has been deactivated");
        }

        if (!passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
            throw new BadCredentialsException("Incorrect username or password");
        }

        return jwtService.generateToken(userDetailsService.loadUserByUsername(user.getEmail()));
    }
}
