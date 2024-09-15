package com.bank.transaction_online_banking_service.controller;

import com.bank.authorization.security.ValidateToken;
import com.bank.transaction_online_banking_service.dto.*;
import com.bank.transaction_online_banking_service.dto.Error;
import com.bank.transaction_online_banking_service.exception.InvalidException;
import com.bank.transaction_online_banking_service.model.ScheduledTransaction;
import com.bank.transaction_online_banking_service.service.FundTransferTransactionService;
import com.bank.transaction_online_banking_service.service.TransactionService;
import com.bank.transaction_online_banking_service.service.impl.TransactionServices;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/transaction")
@Slf4j
public class TransactionController {
    private static final String STATUS="SUCCESS";
    @Autowired
    ValidateToken validateToken;

//    @Autowired
//    private TokenExtractionService tokenExtractionService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TransactionServices transactionServices;

    @Autowired
    @Qualifier("immediateFundTransferTransactionServiceImpl")
    private FundTransferTransactionService immediateFundTransferTransactionService;
    Logger logger = LoggerFactory.getLogger(TransactionController.class);
    @PostMapping("/deposit")
    @PreAuthorize("hasAuthority('USER')")
    @CircuitBreaker(name = "fall-back", fallbackMethod = "fallbackMethod")
    public ResponseEntity<ApiResponse<SummaryDetails>> depositTransaction(
            HttpServletRequest httpServletRequest,@Valid @RequestBody  DepositRequest depositRequest)
            throws InvalidException {
        logger.info("depositTransaction controller starts");
        logger.info("Transaction Controller: Deposit Transaction(-); Transaction Deposit Data: "+ depositRequest.toString());
        String token = httpServletRequest.getHeader("Authorization").substring(7);
        String userId=validateToken.extractUserName(token);
        SummaryDetails summaryDetails=transactionService.doTransaction(userId,depositRequest);
        ApiResponse<SummaryDetails> response=new ApiResponse<>(STATUS,summaryDetails,null);
        logger.info("depositTransaction controller ends");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/fund-transfer/immediate")
    @PreAuthorize("hasAuthority('USER')")
    @CircuitBreaker(name = "fall-back", fallbackMethod = "fallbackMethod")
    public ResponseEntity<ApiResponse<SummaryDetails>> immediateFundTransferTransaction(
            HttpServletRequest httpServletRequest, @RequestBody FundTransfer fundTransfer) throws InvalidException, FeignException {
        logger.info("immediateFundTransferTransaction controller starts");
        logger.info("Transaction Controller: Immediate Fund Transfer Transaction(-); Transaction Immediate Fund Transfer Data: "+ fundTransfer.toString());

        String token= httpServletRequest.getHeader("Authorization").substring(7);
        String userId=validateToken.extractUserName(token);
        SummaryDetails summaryDetails=immediateFundTransferTransactionService.
                doFundTransaction(fundTransfer,userId);
        ApiResponse<SummaryDetails> response=new ApiResponse<>(STATUS,summaryDetails,null);
        logger.info("immediateFundTransferTransaction controller ends");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/fund-transfer/scheduled")
    @PreAuthorize("hasAuthority('USER')")
    @CircuitBreaker(name = "fallback", fallbackMethod = "fallbackMethod")
    public ResponseEntity<ApiResponse<ScheduledTransaction>>scheduleFundTransfer(@Valid @RequestBody ScheduledTransactionDto scheduledTransaction
            , HttpServletRequest httpServletRequest) throws InvalidException {
        logger.info("ScheduledFund Transfer controller starts");

        logger.info("Transaction Controller: Scheduled Fund Transfer Transaction(-); Transaction Scheduled Fund Transfer Data: "+ scheduledTransaction.toString());

        String token=httpServletRequest.getHeader("Authorization").substring(7) ;
        scheduledTransaction.setFromUserId(validateToken.extractUserName(token));
        ScheduledTransaction scheduledTrans =	transactionServices.scheduleFundTransfer(scheduledTransaction);
        ApiResponse<ScheduledTransaction> response = new ApiResponse<>("success", scheduledTrans, null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<ApiResponse<String>> fallbackMethod(CallNotPermittedException exception){

        logger.info(" Fallback(-); Fallback Data: Fallback Initiated");
        Error<String> errorMessage=new Error(1001, "Unavailable Service");
        ApiResponse<String> response=new ApiResponse<>("Error",null, List.of(errorMessage));
        return ResponseEntity.status(org.springframework.http.HttpStatus.GATEWAY_TIMEOUT).body(response);

    }

}