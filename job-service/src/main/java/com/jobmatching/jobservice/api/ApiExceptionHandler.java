package com.jobmatching.jobservice.api;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.*;

@RestControllerAdvice
public class ApiExceptionHandler {

    private Map<String, Object> body(HttpStatus status, String message, HttpServletRequest req) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("timestamp", Instant.now().toString());
        m.put("status", status.value());
        m.put("error", status.getReasonPhrase());
        m.put("message", message);
        m.put("path", req.getRequestURI());
        return m;
    }

    // 404 – job không tồn tại
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> notFound(EntityNotFoundException ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(body(HttpStatus.NOT_FOUND, ex.getMessage(), req));
    }

    // 400 – Bean Validation (@Valid) lỗi field
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> invalidArgs(MethodArgumentNotValidException ex, HttpServletRequest req) {
        Map<String, Object> m = body(HttpStatus.BAD_REQUEST, "Validation failed", req);
        List<Map<String, String>> errors = new ArrayList<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            errors.add(Map.of(
                    "field", fe.getField(),
                    "message", fe.getDefaultMessage() == null ? "invalid" : fe.getDefaultMessage()));
        }
        m.put("fieldErrors", errors);
        return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(m);
    }

    // 400 – JSON sai định dạng, enum sai, số sai kiểu…
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> jsonUnreadable(HttpMessageNotReadableException ex, HttpServletRequest req) {
        String msg = "Malformed JSON request";
        if (ex.getCause() instanceof InvalidFormatException ife) {
            // enum sai, kiểu sai
            msg = "Invalid value for field '" +
                    (ife.getPath().isEmpty() ? "?" : ife.getPath().get(0).getFieldName()) +
                    "'";
        }
        return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON)
                .body(body(HttpStatus.BAD_REQUEST, msg, req));
    }

    // 400 – query/path param sai kiểu
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> typeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest req) {
        return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON)
                .body(body(HttpStatus.BAD_REQUEST, "Invalid parameter: " + ex.getName(), req));
    }

    // 415 – không gửi Content-Type: application/json
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<?> mediaType(HttpMediaTypeNotSupportedException ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .contentType(MediaType.APPLICATION_JSON)
                .body(body(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Content-Type must be application/json", req));
    }

    // 400 – lỗi ràng buộc DB (FK employerId, duplicate composite key job_skills, …)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> dataIntegrity(DataIntegrityViolationException ex, HttpServletRequest req) {
        String message = "Invalid reference or constraint violation";
        return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON)
                .body(body(HttpStatus.BAD_REQUEST, message, req));
    }

    // 400 – business rule (salaryMin > salaryMax, …)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> badRequest(IllegalArgumentException ex, HttpServletRequest req) {
        return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON)
                .body(body(HttpStatus.BAD_REQUEST, ex.getMessage(), req));
    }
}
