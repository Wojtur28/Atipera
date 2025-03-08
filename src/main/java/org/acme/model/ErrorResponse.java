package org.acme.model;

public record ErrorResponse(int statusCode,
                           String message) {
}
