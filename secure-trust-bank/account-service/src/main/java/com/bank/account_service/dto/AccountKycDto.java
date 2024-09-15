package com.bank.account_service.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountKycDto {
    private String accountNumber;
    private String kyc;

}
