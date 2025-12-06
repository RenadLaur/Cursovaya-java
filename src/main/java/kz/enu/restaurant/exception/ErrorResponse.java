package kz.enu.restaurant.exception;

import java.time.LocalDateTime;

/**
 * Структура ответа об ошибке для REST-клиентов.
 * Возвращается глобальным обработчиком исключений.
 */
public class ErrorResponse {

    private final String message;
    private final String details;
    private final LocalDateTime timestamp;

    public ErrorResponse(String message, String details) {
        this.message = message;
        this.details = details;
        this.timestamp = LocalDateTime.now();
    }

    public String getMessage() {
        return message;
    }

    public String getDetails() {
        return details;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
