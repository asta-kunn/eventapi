package com.example.eventapi.service;

import com.example.eventapi.dto.StatsResponse;
import com.example.eventapi.entity.*;
import com.example.eventapi.repository.*;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.*;

@Service
public class StatsService {
    private final EventRepository eventRepo;
    private final EventRegistrationRepository regRepo;

    public StatsService(EventRepository eventRepo,
                        EventRegistrationRepository regRepo) {
        this.eventRepo = eventRepo;
        this.regRepo   = regRepo;
    }

    public StatsResponse getStats() {
        List<Event> events = eventRepo.findAll();
        List<EventRegistration> regs = regRepo.findAll();

        long totalEvents = events.size();
        long totalRegs   = regs.size();

        Map<Long, Long> counts = regs.stream()
            .collect(Collectors.groupingBy(r -> r.getEvent().getId(), Collectors.counting()));

        double avg = counts.values().stream()
                           .mapToLong(Long::longValue)
                           .average()
                           .orElse(0);

        List<StatsResponse.PopularEvent> top3 = counts.entrySet().stream()
            .sorted(Map.Entry.<Long,Long>comparingByValue().reversed())
            .limit(3)
            .map(e -> {
                Event ev = events.stream()
                                 .filter(x -> x.getId().equals(e.getKey()))
                                 .findFirst().orElseThrow();
                return new StatsResponse.PopularEvent(ev.getId(), ev.getTitle(), e.getValue());
            }).collect(Collectors.toList());

        return StatsResponse.builder()
            .totalEvents(totalEvents)
            .totalRegistrations(totalRegs)
            .averageRegistrationsPerEvent(avg)
            .top3Events(top3)
            .build();
    }
}
