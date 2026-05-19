package com.library.librarymanager.Exception;





import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 🔥 VALIDATION ERROR
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

    // 🔥 AUTH ERROR
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