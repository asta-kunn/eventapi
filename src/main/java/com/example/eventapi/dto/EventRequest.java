package com.example.eventapi.dto;

import lombok.Data;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Data
public class EventRequest {
    @NotBlank private String title;
    private String description;
    @NotNull @Future private LocalDateTime date;
}
