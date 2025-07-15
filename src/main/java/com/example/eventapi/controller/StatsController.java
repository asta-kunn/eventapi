package com.example.eventapi.controller;

import com.example.eventapi.dto.StatsResponse;
import com.example.eventapi.service.StatsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/events/stats")
public class StatsController {
    private final StatsService svc;
    public StatsController(StatsService svc) { this.svc = svc; }

    @GetMapping
    public StatsResponse stats() {
        return svc.getStats();
    }
}
