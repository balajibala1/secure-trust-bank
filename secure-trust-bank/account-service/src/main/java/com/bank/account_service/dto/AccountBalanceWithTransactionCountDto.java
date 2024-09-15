package com.bank.account_service.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountBalanceWithTransactionCountDto {
    private String accountNumber;
    private Double accountBalance;
    private Integer transactionCount;
}
