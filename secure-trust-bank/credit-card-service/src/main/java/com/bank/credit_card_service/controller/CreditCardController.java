package com.bank.credit_card_service.controller;

import com.bank.credit_card_service.dto.ApiResponse;
import com.bank.credit_card_service.dto.FeignDto;
import com.bank.credit_card_service.exception.AlreadyAppliedException;
import com.bank.credit_card_service.exception.CustomException;
import com.bank.credit_card_service.model.CreditCard;
import com.bank.credit_card_service.service.CreditCardService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/credit-card")
@AllArgsConstructor
@Slf4j
public class CreditCardController {
    private final CreditCardService creditCardService;

    @PostMapping("/apply")
    public ResponseEntity<ApiResponse<String>> applyCreditCard(HttpServletRequest request) throws AlreadyAppliedException {
        String token =request.getHeader("Authorization");
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.successHandler(creditCardService.applyCreditCard(token.substring(7))));
    }
    @PostMapping("/generate/credit-card/number")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> approvePendingApplication(@RequestBody FeignDto feignDto){
        System.out.println("The processId: for LoadBalancer for approvePendingApplication is: "+ProcessHandle.current().pid());
        System.out.println(feignDto.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(creditCardService.approvePendingApplication(feignDto.getApplicationNumber(),feignDto.getUserId()));
    }
    @GetMapping("/details/{cardNumber}")
    @PreAuthorize("hasAuthority('USER')")
    public Optional<CreditCard> getCreditCardDetails(@PathVariable("cardNumber") String cardNumber){
        System.out.println("The processId: for LoadBalancer for GetCreditCardDetails is: "+ProcessHandle.current().pid());
        return creditCardService.getCreditCardNumber(cardNumber);
    }
    @PutMapping("/update-available-limit")
    @PreAuthorize("hasAuthority('USER')")
    public void updateAvailableLimit(@RequestBody CreditCard creditCard) throws CustomException {
        System.out.println("The processId: for LoadBalancer for UpdateAvailableLimit is: "+ProcessHandle.current().pid());
        creditCardService.updateAvailableLimit(creditCard);
    }

    @GetMapping("/get/detail")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<ApiResponse> getCreditCardDetails(HttpServletRequest request){
        String token =request.getHeader("Authorization");
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.successHandler(creditCardService.getAllCreditCardDetails(token.substring(7))));

    }

}