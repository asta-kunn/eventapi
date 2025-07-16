package com.example.eventapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.eventapi.dto.ApiResponse;
import com.example.eventapi.dto.JwtResponse;
import com.example.eventapi.dto.LoginRequest;
import com.example.eventapi.dto.RegisterRequest;
import com.example.eventapi.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    public AuthController(AuthService authService) { this.authService = authService; }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<JwtResponse>> register(
            @Valid @RequestBody RegisterRequest req) {
        JwtResponse res = authService.register(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(res, "User registered successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtResponse>> login(
            @Valid @RequestBody LoginRequest req) {
        JwtResponse res = authService.login(req);
        return ResponseEntity.ok(ApiResponse.success(res, "Login successful"));
    }
}
