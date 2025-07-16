package com.example.eventapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
public class RegisterRequest {
    @NotBlank private String name;
    @Email @NotBlank private String email;
    @NotBlank @Size(min = 6) private String password;
    private String role; // No default value, let it be null if not provided
}
