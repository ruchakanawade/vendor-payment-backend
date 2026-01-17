package com.vendor.vendor_payment_system.controller;

import com.vendor.vendor_payment_system.config.security.JwtTokenProvider;
import com.vendor.vendor_payment_system.dto.LoginRequest;
import com.vendor.vendor_payment_system.dto.LoginResponse;
import com.vendor.vendor_payment_system.entity.UserEntity;
import com.vendor.vendor_payment_system.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthController(
            UserRepository userRepository,
            JwtTokenProvider jwtTokenProvider) {

        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {

        UserEntity user = userRepository
                .findByUsernameAndActiveTrue(request.getUsername())
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        String token = jwtTokenProvider.generateToken(user.getId(), user.getUsername());
        return new LoginResponse(token);
    }
}
