package com.example.eventapi.service;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.eventapi.dto.EventRequest;
import com.example.eventapi.dto.EventResponse;
import com.example.eventapi.entity.Event;
import com.example.eventapi.entity.EventRegistration;
import com.example.eventapi.entity.User;
import com.example.eventapi.exception.ResourceNotFoundException;
import com.example.eventapi.mapper.EventMapper;
import com.example.eventapi.repository.EventRegistrationRepository;
import com.example.eventapi.repository.EventRepository;
import com.example.eventapi.repository.UserRepository;

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
        // Debug logging
        System.out.println("DEBUG: User trying to create event: " + auth.getName());
        System.out.println("DEBUG: User authorities: " + auth.getAuthorities());
        
        // Check if user has ADMIN role
        if (!auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            System.out.println("DEBUG: User does not have ROLE_ADMIN authority, rejecting");
            throw new IllegalArgumentException("Only ADMIN users can create events");
        }
        
        System.out.println("DEBUG: User has ADMIN authority, proceeding with event creation");
        
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
