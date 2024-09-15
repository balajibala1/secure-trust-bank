package com.bank.transaction_credit_card_service.controller;

import com.bank.transaction_credit_card_service.dto.ApiResponse;
import com.bank.transaction_credit_card_service.dto.TransactionDto;
import com.bank.transaction_credit_card_service.dto.TransactionResponse;
import com.bank.transaction_credit_card_service.exception.CustomException;
import com.bank.transaction_credit_card_service.service.PurchaseService;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@AllArgsConstructor
public class PurchaseController {
    private final PurchaseService purchaseService;

    @PostMapping("/purchase")
    @PreAuthorize("hasAuthority('USER')")
    @CircuitBreaker(name = "CREDIT-SERVICE",fallbackMethod = "getCreditCardDetailsFallBack")
    public ResponseEntity<ApiResponse<HashMap<String, Double>>> purchase
            (@Valid @RequestBody TransactionDto transaction)
            throws CustomException {
        System.out.println("The processId: for LoadBalancer for purchase is: "+ProcessHandle.current().pid());
        HashMap<String, Double> result = purchaseService.purchase(transaction);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.successHandler(result));
    }
    public ResponseEntity<ApiResponse<TransactionResponse>> getCreditCardDetailsFallBack(CallNotPermittedException exception){
        return new ResponseEntity<>(ApiResponse.failureHandler("Try again after SomeTime", HttpStatus.NOT_FOUND),
                HttpStatus.NOT_FOUND);
    }
}
