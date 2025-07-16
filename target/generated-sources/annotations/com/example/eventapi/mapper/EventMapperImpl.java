package com.example.eventapi.mapper;

import com.example.eventapi.dto.EventRequest;
import com.example.eventapi.dto.EventResponse;
import com.example.eventapi.entity.Event;
import com.example.eventapi.entity.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-16T13:10:43+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.2 (Oracle Corporation)"
)
@Component
public class EventMapperImpl implements EventMapper {

    @Override
    public Event toEntity(EventRequest dto) {
        if ( dto == null ) {
            return null;
        }

        Event.EventBuilder event = Event.builder();

        event.title( dto.getTitle() );
        event.description( dto.getDescription() );
        event.date( dto.getDate() );

        return event.build();
    }

    @Override
    public EventResponse toDto(Event entity) {
        if ( entity == null ) {
            return null;
        }

        EventResponse eventResponse = new EventResponse();

        eventResponse.setCreatedBy( entityCreatedById( entity ) );
        eventResponse.setId( entity.getId() );
        eventResponse.setTitle( entity.getTitle() );
        eventResponse.setDescription( entity.getDescription() );
        eventResponse.setDate( entity.getDate() );

        return eventResponse;
    }

    private Long entityCreatedById(Event event) {
        if ( event == null ) {
            return null;
        }
        User createdBy = event.getCreatedBy();
        if ( createdBy == null ) {
            return null;
        }
        Long id = createdBy.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
