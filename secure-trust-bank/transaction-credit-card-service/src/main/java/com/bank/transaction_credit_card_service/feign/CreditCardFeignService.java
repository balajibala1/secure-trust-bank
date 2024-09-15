package com.bank.transaction_credit_card_service.feign;

import com.bank.transaction_credit_card_service.dto.CreditCardDto;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@FeignClient(name="CREDIT-SERVICE",path ="/credit-card" )
@LoadBalancerClient(name = "CREDIT-SERVICE")
public interface CreditCardFeignService {
    @PutMapping("/update-available-limit")
    void updateAvailableLimit(@RequestBody CreditCardDto creditCard);

    @GetMapping("/details/{cardNumber}")
    Optional<CreditCardDto> getCreditCardDetails(@PathVariable("cardNumber") String cardNumber);
}
