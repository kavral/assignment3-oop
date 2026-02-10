package exception;

import logging.LoggerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final LoggerService logger = LoggerService.getInstance();

    @ExceptionHandler({FoodItemNotValidException.class, ValidationException.class})
    public ResponseEntity<Map<String, Object>> handleDomainValidation(RuntimeException ex) {
        logger.error("Domain validation error", ex);
        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleBeanValidation(MethodArgumentNotValidException ex) {
        logger.error("Bean validation error", ex);
        String message = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(err -> err.getField() + " " + err.getDefaultMessage())
                .orElse("Validation failed");
        return buildError(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
        logger.error("Unexpected error", ex);
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
    }

    private ResponseEntity<Map<String, Object>> buildError(HttpStatus status, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        return ResponseEntity.status(status).body(body);
    }
}

