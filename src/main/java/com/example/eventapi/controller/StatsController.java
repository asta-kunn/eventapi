package com.example.eventapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.eventapi.dto.ApiResponse;
import com.example.eventapi.dto.StatsResponse;
import com.example.eventapi.service.StatsService;

@RestController
@RequestMapping("/api/events/stats")
public class StatsController {
    private final StatsService svc;
    public StatsController(StatsService svc) { this.svc = svc; }

    @GetMapping
    public ResponseEntity<ApiResponse<StatsResponse>> stats() {
        StatsResponse stats = svc.getStats();
        return ResponseEntity.ok(ApiResponse.success(stats, "Statistics retrieved successfully"));
    }
}
