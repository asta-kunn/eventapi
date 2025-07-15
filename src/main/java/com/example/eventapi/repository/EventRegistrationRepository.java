package com.example.eventapi.repository;

import com.example.eventapi.entity.EventRegistration;
import com.example.eventapi.entity.User;
import com.example.eventapi.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface EventRegistrationRepository extends JpaRepository<EventRegistration,Long> {
    Optional<EventRegistration> findByUserAndEvent(User user, Event event);
    List<EventRegistration> findAll();
}
