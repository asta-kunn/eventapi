package com.example.eventapi.dto;

import lombok.*;
import java.util.List;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class StatsResponse {
    private long totalEvents;
    private long totalRegistrations;
    private double averageRegistrationsPerEvent;
    private List<PopularEvent> top3Events;

    @Data @AllArgsConstructor @NoArgsConstructor
    public static class PopularEvent {
        private Long eventId;
        private String title;
        private long registrations;
    }
}
