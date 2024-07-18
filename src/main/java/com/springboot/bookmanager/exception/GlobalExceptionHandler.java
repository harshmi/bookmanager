package com.springboot.bookmanager.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleEntityNotFound(EntityNotFoundException ex) {
        ErrorResponseDto ErrorResponseDto = new ErrorResponseDto();
        ErrorResponseDto.setMessage("Entity not found: "+ ex.getMessage());
        ErrorResponseDto.setTimestamp(LocalDateTime.now().toString());
        ErrorResponseDto.setErrorCode(String.valueOf(HttpStatus.NOT_FOUND));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponseDto);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseDto> handleConstraintViolation(ConstraintViolationException ex) {
        ErrorResponseDto ErrorResponseDto = new ErrorResponseDto();
        ErrorResponseDto.setMessage("Validation failed: "+ ex.getMessage());
        ErrorResponseDto.setTimestamp(LocalDateTime.now().toString());
        ErrorResponseDto.setErrorCode(String.valueOf(HttpStatus.BAD_REQUEST));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponseDto);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponseDto> handleRuntimeException(RuntimeException ex) {
        ErrorResponseDto ErrorResponseDto = new ErrorResponseDto();
        ErrorResponseDto.setMessage("Internal server error: "+ ex.getMessage());
        ErrorResponseDto.setTimestamp(LocalDateTime.now().toString());
        ErrorResponseDto.setErrorCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorResponseDto);
    }

}
