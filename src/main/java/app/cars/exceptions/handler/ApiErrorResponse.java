package app.cars.exceptions.handler;

import java.time.LocalDateTime;

public record ApiErrorResponse (
        LocalDateTime timestamp,
        int status,
        String message
        ){}
