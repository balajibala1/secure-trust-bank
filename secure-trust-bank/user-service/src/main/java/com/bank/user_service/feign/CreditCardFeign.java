package com.bank.user_service.feign;

import com.bank.user_service.dto.ApiResponse;
import com.bank.user_service.dto.FeignDto;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@FeignClient(name = "CREDIT-SERVICE",path ="/credit-card" )
public interface CreditCardFeign {
    @PostMapping("/generate/credit-card/number")
    @CircuitBreaker(name="CREDIT-SERVICE",fallbackMethod = "fallBackMethodForCreditCardNumberGeneration")
    String approvePendingApplication(@RequestBody FeignDto feignDto);

    default ResponseEntity<ApiResponse<String>> fallBackMethodForCreditCardNumberGeneration(
            CallNotPermittedException exception){
        return new ResponseEntity<>(ApiResponse.failureHandler("Try again after SomeTime",
                HttpStatus.INTERNAL_SERVER_ERROR.value()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}