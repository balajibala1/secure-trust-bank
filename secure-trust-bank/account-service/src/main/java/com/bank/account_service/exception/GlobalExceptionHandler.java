package com.bank.account_service.exception;

import com.bank.account_service.dto.ApiResponse;
import com.bank.account_service.dto.Error;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    private final String fail = "Failure";

    @ExceptionHandler({AccountAlreadyExistException.class})
    public ResponseEntity<ApiResponse> handleAccountAlreadyExistException(AccountAlreadyExistException exception) {
        ApiResponse response = new ApiResponse();
        response.setStatus(fail);
        response.setData(null);
        List<Error> errors = new ArrayList<>();
        errors.add(new Error(406, exception.getLocalizedMessage()));
        response.setErrors(errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler({AlreadyAppliedException.class})
    public ResponseEntity<ApiResponse> AlreadyAppliedException(AlreadyAppliedException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.failureHandler(exception.getMessage(), 406));
    }

    @ExceptionHandler({AccountNotPresentException.class})
    public ResponseEntity<ApiResponse> accountNotPresentException(AccountNotPresentException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.failureHandler(exception.getMessage(), 404));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        List<Error> errors = new ArrayList<>();
        errors.add(new Error(401, "Enter valid Data"));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Failure", null, errors));
    }

    @ExceptionHandler(InvalidDataAccessApiUsageException.class)
    public ResponseEntity<ApiResponse> handleInvalidDataAccessApiUsageException(InvalidDataAccessApiUsageException exception) {
        List<Error> errors = new ArrayList<>();
        errors.add(new Error(401, "Invalid id"));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Failure", null, errors));
    }
}
