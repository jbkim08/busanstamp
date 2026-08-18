package com.example.busanstamp.common;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //커스텀 예외 발생시
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApiException(ApiException exception) {
        ErrorResponse response = new ErrorResponse(
                exception.getStatus().value(),
                exception.getMessage(),
                LocalDateTime.now());

        return ResponseEntity.status(exception.getStatus()).body(response);
    }

    //유효성 검사 에러시
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException exception) {
        FieldError fieldError = exception.getBindingResult().getFieldError();

        String message = fieldError == null ?
                "입력값을 확인해주세요." : fieldError.getDefaultMessage();

        ErrorResponse response = new ErrorResponse(400, message, LocalDateTime.now());

        return ResponseEntity.badRequest().body(response);
    }
}