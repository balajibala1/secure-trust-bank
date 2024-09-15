package com.bank.auth_service.exception;


import com.bank.auth_service.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Object> userNotFoundException(UsernameNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.failureHandler(ex.getMessage(), HttpStatus.UNAUTHORIZED));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> badCredentialException(BadCredentialsException ex){
        log.error("throwing badCredential exception");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.failureHandler(ex.getMessage(), HttpStatus.BAD_REQUEST));

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> methodArgumentValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        log.error("throwing methodArgumentValidation exception");
        ex.getBindingResult().getFieldErrors().forEach(er -> errors.put(er.getField(), er.getDefaultMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.failureHandler(ex.getMessage(), HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> httpMessageNotReadableException(HttpMessageNotReadableException ex){
        log.error("throwing httpMessageNotReadableException exception");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.failureHandler(ex.getMessage(), HttpStatus.BAD_REQUEST));
    }

}