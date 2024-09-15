package com.bank.transaction_online_banking_service.feign;

import com.bank.transaction_online_banking_service.dto.*;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@FeignClient(name = "ACCOUNT-SERVICE")
public interface AccountService {

    @PostMapping("/account/accountDetails")
    ResponseEntity<ApiResponse<List<AccountDetails>>> accountDetails(@RequestBody UserDto userDto);
    @PostMapping("/account/account-details/accountNumber")
    ResponseEntity<ApiResponse<AccountDetails>> accountDetailsByAccountNumber(@RequestBody AccountDetailsRequest accountDetailsRequest);

    @PutMapping("/account/updated-balance")
    ResponseEntity<ApiResponse<AccountDetails>> updateAccountBalance(@RequestBody AccountBalanceUpdate accountBalanceUpdate);
}
