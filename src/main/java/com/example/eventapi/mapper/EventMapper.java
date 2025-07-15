package com.example.eventapi.mapper;

import com.example.eventapi.entity.Event;
import com.example.eventapi.dto.*;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface EventMapper {
    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

    @Mapping(target = "createdBy", ignore = true)
    Event toEntity(EventRequest dto);

    @Mapping(source = "createdBy.id", target = "createdBy")
    EventResponse toDto(Event entity);
}
