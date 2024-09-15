package com.bank.account_service.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto {
    private String accountNumber;
    private String userId;
    private Double accountBalance;
    private String accountStatus;
    private String kyc;
    private Integer transactionCount;
}
