package com.bank.transaction_online_banking_service.exception;

import com.bank.transaction_online_banking_service.dto.ApiResponse;
import com.bank.transaction_online_banking_service.dto.Error;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<String>> handleInvalidException(MethodArgumentNotValidException exception){
        Map<String, String> error=new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(errorMap-> error.put(errorMap.getField(),
                errorMap.getDefaultMessage()));
        Error<Map<String, String>> errorMessage=new Error(1001, error);
        List<Error> list = new ArrayList<>();
        list.add(errorMessage);
        ApiResponse<String> response=new ApiResponse<>("Error",null,list);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

    }
    @ExceptionHandler({AccountNotPresentException.class})
    public ResponseEntity<ApiResponse> accountNotPresentException(AccountNotPresentException exception){
        ApiResponse response=new ApiResponse();
        response.setStatus("Failure");
        response.setData(null);
        Error errorResponse = new Error(HttpStatus.BAD_REQUEST.value(), exception.getLocalizedMessage());
        List<Error> list = new ArrayList<>();
        list.add(errorResponse);
        response.setErrors(list);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler({InvalidException.class})
    public ResponseEntity<ApiResponse<String>> handleInvalidException(InvalidException exception){

        Error<String> errorMessage=new Error(1001, exception.getMessage());
        List<Error> list = new ArrayList<>();
        list.add(errorMessage);
        ApiResponse<String> response=new ApiResponse<>("Error",null,list);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<ApiResponse<String>> handleInvalid(IllegalArgumentException exception){

        Error<String> errorMessage=new Error(1001, exception.getMessage());
        List<Error> list = new ArrayList<>();
        list.add(errorMessage);
        ApiResponse<String> response=new ApiResponse<>("Error",null,list);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

    }
    @ExceptionHandler({SignatureException.class})
    public ResponseEntity<ApiResponse<String>> signature(SignatureException exception){
        Error<String> errorMessage=new Error(1001, "Signature does not match");
        List<Error> list = new ArrayList<>();
        list.add(errorMessage);
        ApiResponse<String> response=new ApiResponse<>("Error",null,list);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler({ExpiredJwtException.class})
    public ResponseEntity<ApiResponse<String>> expired(ExpiredJwtException exception){
        Error<String> errorMessage=new Error(1001, "Token Expired");
        List<Error> list = new ArrayList<>();
        list.add(errorMessage);
        ApiResponse<String> response=new ApiResponse<>("Error",null,list);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler({FeignException.NotFound.class, FeignException.BadRequest.class})
    public ResponseEntity<ApiResponse<String>> handleFeignNotFound(FeignException exception){
        Error<String> errorMessage=new Error(1001,"Invalid Data Provided");
        List<Error> list = new ArrayList<>();
        list.add(errorMessage);
        exception.printStackTrace();
        ApiResponse<String> response=new ApiResponse<>("Error",null,list);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(FeignException.ServiceUnavailable.class)
    public ResponseEntity<ApiResponse<String>> handleNotFound(FeignException.ServiceUnavailable exception){

        Error<String> errorMessage=new Error(1001,"Service Unavailable ");
        List<Error> list = new ArrayList<>();
        list.add(errorMessage);
        ApiResponse<String> response=new ApiResponse<>("Error",null,list);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<String>> handleAccessDeniedException(AccessDeniedException e){
        Error<String> errorMessage=new Error(1001,"Access Denied");
        List<Error> list = new ArrayList<>();
        list.add(errorMessage);
        ApiResponse<String> response=new ApiResponse<>("Error",null,list);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

}