package com.example.eventapi.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resource, String field, Object val) {
        super(String.format("%s not found with %s = %s", resource, field, val));
    }
}
