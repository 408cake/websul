package com.web.bookstore.common;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.time.OffsetDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApi(ApiException e, HttpServletRequest req) {
        ErrorCode ec = e.getErrorCode();
        Object details = e.getDetails();
        return ResponseEntity.status(ec.status())
                .body(new ErrorResponse(OffsetDateTime.now(), req.getRequestURI(), ec.status().value(), ec.name(), ec.message(), details));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleBody(HttpMessageNotReadableException e, HttpServletRequest req) {
        ErrorCode ec = ErrorCode.INVALID_REQUEST_BODY;
        return ResponseEntity.status(ec.status())
                .body(new ErrorResponse(OffsetDateTime.now(), req.getRequestURI(), ec.status().value(), ec.name(), ec.message(), null));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValid(MethodArgumentNotValidException e, HttpServletRequest req) {
        ErrorCode ec = ErrorCode.INVALID_REQUEST_BODY;
        return ResponseEntity.status(ec.status())
                .body(new ErrorResponse(OffsetDateTime.now(), req.getRequestURI(), ec.status().value(), ec.name(), ec.message(), null));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnknown(Exception e, HttpServletRequest req) {
        ErrorCode ec = ErrorCode.INTERNAL_SERVER_ERROR;
        String detail = e.getClass().getName();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(OffsetDateTime.now(), req.getRequestURI(), ec.status().value(), ec.name(), ec.message(), detail));
    }
}
