package com.coding.fitness.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.time.LocalDateTime;

@ControllerAdvice
public class ExceptionHandler {


    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ResponseEntity<Object> globalExceptionHandler(Exception ex){
        return new ResponseEntity<Object>(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), LocalDateTime.now()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @org.springframework.web.bind.annotation.ExceptionHandler(ValidationException.class)
    public ResponseEntity<Object> validationExceptionHandler(ValidationException ex){
        return new ResponseEntity<Object>(new ErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), LocalDateTime.now()), HttpStatus.NOT_FOUND);

    }
    @org.springframework.web.bind.annotation.ExceptionHandler(ProcessingImgException.class)
    public ResponseEntity<Object> errorProcessingImgExceptionHandler(ValidationException ex){
        return new ResponseEntity<Object>(new ErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage(), LocalDateTime.now()), HttpStatus.NOT_FOUND);

    }
}
