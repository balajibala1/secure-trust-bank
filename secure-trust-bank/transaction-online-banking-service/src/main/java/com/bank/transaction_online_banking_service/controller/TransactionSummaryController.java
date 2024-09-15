package com.bank.transaction_online_banking_service.controller;


import com.bank.authorization.security.ValidateToken;
import com.bank.transaction_online_banking_service.dto.*;
import com.bank.transaction_online_banking_service.dto.Error;
import com.bank.transaction_online_banking_service.exception.InvalidException;
import com.bank.transaction_online_banking_service.service.TokenExtractionService;
import com.bank.transaction_online_banking_service.service.TransactionSummaryService;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/transaction")
public class TransactionSummaryController {
    private static final String STATUS="SUCCESS";
    @Autowired
    private TokenExtractionService tokenExtractionService;
    Logger logger = LoggerFactory.getLogger(TransactionSummaryController.class);

    @Autowired
    private TransactionSummaryService transactionSummaryService;

    @Autowired
    private ValidateToken validateToken;

    @PostMapping("/transaction-summary")
    @PreAuthorize("hasAuthority('USER')")
    @CircuitBreaker(name = "fall-back", fallbackMethod = "fallbackMethod")
    public ResponseEntity<ApiResponse<SummaryDetails>> getTransactionSummary(@RequestBody AccountDetailsRequest accountNumber, HttpServletRequest httpServletRequest)
            throws InvalidException {
        logger.info("ScheduledFund Transfer controller starts");
        logger.info("Transaction Summary Controller: Monthly Summary Transaction(-); Transaction Monthly Summary]");
//        TokenDetails tokenDetails= tokenExtractionService.extractToken(httpServletRequest);
        String token=httpServletRequest.getHeader("Authorization").substring(7);
        String userId=validateToken.extractUserName(token);
        //AccountDetailsRequest accountDetailsRequest = new AccountDetailsRequest(accountNumber);
        SummaryDetails transactionSummary=transactionSummaryService.transactionSummary(accountNumber);
        ApiResponse<SummaryDetails> response=new ApiResponse<>(STATUS,transactionSummary,null);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public ResponseEntity<ApiResponse<String>> fallbackMethod(CallNotPermittedException exception){

        logger.info(" Fallback(-); Fallback Data: Fallback Initiated");


        Error<String> errorMessage=new Error(1001, "Unavailable Service");
        ApiResponse<String> response=new ApiResponse<>("Error",null, List.of(errorMessage));
        return ResponseEntity.status(org.springframework.http.HttpStatus.GATEWAY_TIMEOUT).body(response);

    }
}