package com.pavell.rickAndMortyApi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler({Exception.class})
    public ResponseEntity<HttpStatus> badRequestException(Exception e, WebRequest request) {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}
