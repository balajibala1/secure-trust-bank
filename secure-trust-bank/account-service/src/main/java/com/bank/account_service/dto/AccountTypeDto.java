package com.bank.account_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AccountTypeDto {

    @NotBlank(message = "Please Enter Account Type")
    //@ValidateAccountType
    private String accountType;
}
