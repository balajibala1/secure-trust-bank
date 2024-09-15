package com.bank.credit_card_service.exception;

import com.bank.credit_card_service.dto.ApiResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.security.auth.login.AccountNotFoundException;
import java.util.HashMap;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {BindException.class})
    public ResponseEntity<ApiResponse<String>> handleBindException(BindException methodArgumentNotValidException) {
        HashMap<String,String> errorMap= new HashMap<>();
        methodArgumentNotValidException.getBindingResult().getFieldErrors().forEach(err->{
            errorMap.put(err.getField(),err.getDefaultMessage());
        });
        log.error("Error While Registering");
        return new ResponseEntity<>(ApiResponse.failureHandler(errorMap,406),HttpStatus.NOT_ACCEPTABLE);
    }
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<String>> transactionCantBeDone(CustomException ex) {
        return new ResponseEntity<>(ApiResponse.failureHandler(ex.getMessage(),406),
                HttpStatus.NOT_ACCEPTABLE);
    }
    @ExceptionHandler(AlreadyAppliedException.class)
    public ResponseEntity<ApiResponse<String>> alreadyApplied(AlreadyAppliedException ex) {
        return new ResponseEntity<>(ApiResponse.failureHandler(ex.getMessage(),406),
                HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleAccountNotFoundException
            (AccountNotFoundException accountNotFoundException){
        log.error(accountNotFoundException.getMessage()+" "+"AccountNotFound Exception");
        return new ResponseEntity<>(ApiResponse.failureHandler(accountNotFoundException.getMessage(),
                401),HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<ApiResponse<String>> handleMalFormedJwtException(MalformedJwtException malformedJwtException){
        log.error("Token Format is Invalid");
        return new ResponseEntity<>(ApiResponse.failureHandler("Invalid Token",401),
                HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ApiResponse<String>> handleExpiredJwtException(ExpiredJwtException exception){
        log.error("Token Expired");
        return new  ResponseEntity<>(ApiResponse.failureHandler("Token is Expired",401),
                HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<ApiResponse<String>> handleSignatureException(io.jsonwebtoken.security.SignatureException signatureException){
        log.error("Token Signature is wrong");
        return new ResponseEntity<>(ApiResponse.failureHandler("Please Enter the Valid Token",401),
                HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<String>> handleAccessDeniedException(AccessDeniedException accessDeniedException){
        log.error("Access Denied");
        return new ResponseEntity<>(ApiResponse.failureHandler("Access Denied",401),
                HttpStatus.UNAUTHORIZED);
    }
}