package com.example.eventapi.repository;

import com.example.eventapi.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event,Long> { }
