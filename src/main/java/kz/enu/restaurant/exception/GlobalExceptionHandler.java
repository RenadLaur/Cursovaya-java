package kz.enu.restaurant.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Глобальный обработчик исключений для всех REST-контроллеров приложения.
 * Преобразует внутренние исключения в понятные JSON-ответы для фронтенда.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Обработка ошибок чтения и парсинга данных меню.
     */
    @ExceptionHandler(MenuDataException.class)
    public ResponseEntity<ErrorResponse> handleMenuDataException(MenuDataException ex) {
        log.error("Ошибка данных меню: {}", ex.getMessage(), ex);
        ErrorResponse body = new ErrorResponse(
                "Ошибка загрузки данных меню",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    /**
     * Общий обработчик для всех неперехваченных исключений.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        log.error("Непредвиденная ошибка приложения", ex);
        ErrorResponse body = new ErrorResponse(
                "Внутренняя ошибка сервера",
                "Попробуйте повторить запрос позже. Если ошибка повторяется — обратитесь к разработчику."
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
