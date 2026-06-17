package com.library.librarymanager.Exception;





import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(err ->
                errors.put(err.getField(), err.getDefaultMessage())
        );

        Map<String, Object> res = new HashMap<>();
        res.put("status", 400);
        res.put("message", "Validation failed");
        res.put("errors", errors);

        return ResponseEntity.badRequest().body(res);
    }

    @ExceptionHandler(org.springframework.web.HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> handleMethodNotSupported(
    org.springframework.web.HttpRequestMethodNotSupportedException ex) {
    return ResponseEntity.status(405).body(Map.of("message", ex.getMessage(), "status", 405));
    }
    
    // 🔥 AUTH ERROR
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<?> handleResponseStatus(ResponseStatusException ex) {
        Map<String, Object> res = new HashMap<>();
        res.put("status", ex.getStatusCode().value());
        res.put("message", ex.getReason() == null ? ex.getMessage() : ex.getReason());
        res.put("errors", new HashMap<>());

        return ResponseEntity
                .status(ex.getStatusCode())
                .body(res);
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<?> handleAuth(AuthException ex) {

        Map<String, Object> res = new HashMap<>();
        res.put("status", HttpStatus.UNAUTHORIZED.value());
        res.put("message", ex.getMessage());
        res.put("errors", new HashMap<>());

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(res);
    }

    // 🔥 RUNTIME ERROR
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDenied(AccessDeniedException ex) {

        Map<String, Object> res = new HashMap<>();
        res.put("status", HttpStatus.FORBIDDEN.value());
        res.put("message", "Tai khoan hien tai khong co quyen thuc hien thao tac nay");
        res.put("errors", new HashMap<>());

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(res);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntime(RuntimeException ex) {

        Map<String, Object> res = new HashMap<>();
        res.put("status", HttpStatus.BAD_REQUEST.value());
        res.put("message", ex.getMessage());
        res.put("errors", new HashMap<>());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(res);
    }

    // 🔥 CATCH ALL ERROR
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleAll(Exception ex) {

        Map<String, Object> res = new HashMap<>();
        res.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        res.put("message", ex.getMessage());
        res.put("errors", new HashMap<>());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(res);
    }
}
