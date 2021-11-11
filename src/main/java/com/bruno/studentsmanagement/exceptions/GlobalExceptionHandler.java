package com.bruno.studentsmanagement.exceptions;

import com.bruno.studentsmanagement.services.exceptions.EmailAlreadyRegisteredException;
import com.bruno.studentsmanagement.services.exceptions.StudentInconsistencyException;
import com.bruno.studentsmanagement.services.exceptions.StudentNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyRegisteredException.class)
    public ResponseEntity<StandardError> emailAlreadyRegistered(
            HttpServletRequest request, EmailAlreadyRegisteredException exception
    ){
        int status = HttpStatus.BAD_REQUEST.value();
        StandardError error = StandardError.builder()
                .timestamp(Instant.now())
                .status(status)
                .error("Bad Request")
                .message(exception.getMessage())
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(StudentInconsistencyException.class)
    public ResponseEntity<StandardError> StudentInconsistency(
            HttpServletRequest request, StudentInconsistencyException exception
    ){
        int status = HttpStatus.BAD_REQUEST.value();
        StandardError error = StandardError.builder()
                .timestamp(Instant.now())
                .status(status)
                .error("Bad Request")
                .message(exception.getMessage())
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<StandardError> StudentNotFound(
            HttpServletRequest request, StudentNotFoundException exception
    ){
        int status = HttpStatus.NOT_FOUND.value();
        StandardError error = StandardError.builder()
                .timestamp(Instant.now())
                .status(status)
                .error("Not Found")
                .message(exception.getMessage())
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardError> validation(MethodArgumentNotValidException exception){
        int status = HttpStatus.BAD_REQUEST.value();
        ValidationError error = ValidationError.builder()
                .timestamp(Instant.now())
                .status(status)
                .error("Bad Request")
                .message(exception.getMessage())
                .build();
        for (FieldError field : exception.getBindingResult().getFieldErrors()){
            error.addError(field.getField(), field.getDefaultMessage());
        }
        return ResponseEntity.status(status).body(error);
    }

}
