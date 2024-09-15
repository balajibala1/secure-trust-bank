package com.bank.user_service.feign;

import com.bank.user_service.dto.AccountDto;
import com.bank.user_service.dto.GetUserDetailDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ACCOUNT-SERVICE")
public interface FeignService {

    @PostMapping("/account/create")
    AccountDto approvePendingAccount(@RequestBody GetUserDetailDto getUserDetailDto);

}
