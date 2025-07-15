package com.example.eventapi.controller;

import com.example.eventapi.dto.*;
import com.example.eventapi.service.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {
    private final EventService svc;
    public EventController(EventService svc) { this.svc = svc; }

    @PostMapping
    public ResponseEntity<EventResponse> create(
            @Valid @RequestBody EventRequest req,
            Authentication auth) {
        return ResponseEntity.status(201).body(svc.createEvent(req, auth));
    }

    @GetMapping
    public List<EventResponse> all() {
        return svc.listAll();
    }

    @GetMapping("/{id}")
    public EventResponse byId(@PathVariable Long id) {
        return svc.getById(id);
    }

    @PostMapping("/{id}/register")
    public ResponseEntity<Void> register(
            @PathVariable Long id,
            Authentication auth) {
        svc.register(id, auth);
        return ResponseEntity.ok().build();
    }
}
