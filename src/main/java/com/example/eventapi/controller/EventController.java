package com.example.eventapi.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.eventapi.dto.ApiResponse;
import com.example.eventapi.dto.EventRequest;
import com.example.eventapi.dto.EventResponse;
import com.example.eventapi.service.EventService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/events")
public class EventController {
    private final EventService svc;
    public EventController(EventService svc) { this.svc = svc; }

    @PostMapping
    public ResponseEntity<ApiResponse<EventResponse>> create(
            @Valid @RequestBody EventRequest req,
            Authentication auth) {
        EventResponse event = svc.createEvent(req, auth);
        return ResponseEntity.status(201).body(ApiResponse.success(event, "Event created successfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<EventResponse>>> all() {
        List<EventResponse> events = svc.listAll();
        return ResponseEntity.ok(ApiResponse.success(events, "Events retrieved successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EventResponse>> byId(@PathVariable Long id) {
        EventResponse event = svc.getById(id);
        return ResponseEntity.ok(ApiResponse.success(event, "Event retrieved successfully"));
    }

    @PostMapping("/{id}/register")
    public ResponseEntity<ApiResponse<Void>> register(
            @PathVariable Long id,
            Authentication auth) {
        svc.register(id, auth);
        return ResponseEntity.ok(ApiResponse.success(null, "Successfully registered for event"));
    }
}
