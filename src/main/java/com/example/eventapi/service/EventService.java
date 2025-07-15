package com.example.eventapi.service;

import com.example.eventapi.dto.*;
import com.example.eventapi.entity.*;
import com.example.eventapi.mapper.EventMapper;
import com.example.eventapi.repository.*;
import com.example.eventapi.exception.ResourceNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.stream.*;

@Service
public class EventService {
    private final EventRepository eventRepo;
    private final UserRepository userRepo;
    private final EventRegistrationRepository regRepo;
    private final EventMapper mapper;

    public EventService(EventRepository eventRepo,
                        UserRepository userRepo,
                        EventRegistrationRepository regRepo,
                        EventMapper mapper) {
        this.eventRepo = eventRepo;
        this.userRepo  = userRepo;
        this.regRepo   = regRepo;
        this.mapper    = mapper;
    }

    @Transactional
    public EventResponse createEvent(EventRequest req, Authentication auth) {
        User creator = userRepo.findByEmail(auth.getName())
            .orElseThrow(() -> new ResourceNotFoundException("User","email",auth.getName()));
        Event e = mapper.toEntity(req);
        e.setCreatedBy(creator);
        return mapper.toDto(eventRepo.save(e));
    }

    public java.util.List<EventResponse> listAll() {
        return eventRepo.findAll().stream()
                        .map(mapper::toDto)
                        .collect(Collectors.toList());
    }

    public EventResponse getById(Long id) {
        Event e = eventRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Event","id",id));
        return mapper.toDto(e);
    }

    @Transactional
    public void register(Long eventId, Authentication auth) {
        User u = userRepo.findByEmail(auth.getName())
            .orElseThrow(() -> new ResourceNotFoundException("User","email",auth.getName()));
        Event e = eventRepo.findById(eventId)
            .orElseThrow(() -> new ResourceNotFoundException("Event","id",eventId));
        if (regRepo.findByUserAndEvent(u,e).isPresent())
            throw new IllegalArgumentException("Already registered");
        EventRegistration reg = EventRegistration.builder()
            .user(u)
            .event(e)
            .registrationDate(LocalDateTime.now())
            .build();
        regRepo.save(reg);
    }
}
