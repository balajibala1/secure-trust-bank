package com.bank.transaction_credit_card_service.controller;

import com.bank.transaction_credit_card_service.dto.ApiResponse;
import com.bank.transaction_credit_card_service.dto.TransactionResponse;
import com.bank.transaction_credit_card_service.exception.CustomException;
import com.bank.transaction_credit_card_service.service.GetTransactionHistoryService;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@AllArgsConstructor
public class GetTransactionHistoryController {
    private GetTransactionHistoryService getTransactionHistoryService;

    @GetMapping("/{creditCardNumber}")
    @PreAuthorize("hasAuthority('USER')")
    @CircuitBreaker(name = "CREDIT-SERVICE",fallbackMethod = "getCreditCardDetailsFallBack")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getCurrentMonthTransactions
            (@PathVariable("creditCardNumber") String creditCardNumber) throws CustomException {
        System.out.println("The processId: for LoadBalancer for getCurrentMonthTransactions is: "+ProcessHandle.current().pid());
        Map<String, Object> result = getTransactionHistoryService.getCurrentMonthTransactions(creditCardNumber);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.successHandler(result));
    }
    public ResponseEntity<ApiResponse<TransactionResponse>> getCreditCardDetailsFallBack(CallNotPermittedException exception){
        return new ResponseEntity<>(ApiResponse.failureHandler("Try again after SomeTime", HttpStatus.NOT_FOUND),
                HttpStatus.NOT_FOUND);
    }
}
