package com.bank.transaction_credit_card_service.exception;

import com.bank.transaction_credit_card_service.dto.ApiResponse;
import com.bank.transaction_credit_card_service.dto.TransactionResponse;
import feign.FeignException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.security.auth.login.AccountNotFoundException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionalHandler {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<TransactionResponse>> transactionCantBeDone(CustomException ex) {
        return new ResponseEntity<>(ApiResponse.failureHandler(ex.getMessage(),HttpStatus.NOT_ACCEPTABLE),
                HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<TransactionResponse>> invalidCreditCardNumber(MethodArgumentNotValidException ex)
    {
        Map<String, String> errorMap = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(err -> errorMap.put(err.getField(), err.getDefaultMessage()));
        return new ResponseEntity<>(ApiResponse.failureHandler(errorMap,HttpStatus.BAD_REQUEST),HttpStatus.NOT_ACCEPTABLE);
    }
    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<ApiResponse<String>> handleMalFormedJwtException(MalformedJwtException malformedJwtException){
        return new ResponseEntity<>(ApiResponse.failureHandler(malformedJwtException.getMessage(),HttpStatus.UNAUTHORIZED)
                ,HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ApiResponse<String>> handleExpiredJwtException(ExpiredJwtException exception){
        return new  ResponseEntity<>(ApiResponse.failureHandler("Token is Expired",HttpStatus.UNAUTHORIZED),
                HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<ApiResponse<String>> handleSignatureException(SignatureException signatureException){
        log.error("Please enter a valid Token : Signature Exception");
        return new ResponseEntity<>(ApiResponse.failureHandler("Please Enter Valid Token",HttpStatus.UNAUTHORIZED),
                HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<String>> handleAccessDeniedException(AccessDeniedException accessDeniedException){
        log.error(accessDeniedException.getMessage()+" "+"AccessDenied Exception");
        return new ResponseEntity<>(ApiResponse.failureHandler(accessDeniedException.getMessage(),HttpStatus.UNAUTHORIZED),
                HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleAccountNotFoundException
            (AccountNotFoundException accountNotFoundException){
        log.error(accountNotFoundException.getMessage()+" "+"AccountNotFound Exception");
        return new ResponseEntity<>(ApiResponse.failureHandler(accountNotFoundException.getMessage(),
                HttpStatus.UNAUTHORIZED),HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(FeignException.ServiceUnavailable.class)
    public ResponseEntity<ApiResponse<TransactionResponse>> handleServiceUnAvailable
            (FeignException.ServiceUnavailable serviceUnavailableException){
        log.error("Service is Unavailable");
        return new ResponseEntity<>(ApiResponse.failureHandler("Service is Down or UnAvailable",
                HttpStatus.SERVICE_UNAVAILABLE),HttpStatus.SERVICE_UNAVAILABLE);
    }

}