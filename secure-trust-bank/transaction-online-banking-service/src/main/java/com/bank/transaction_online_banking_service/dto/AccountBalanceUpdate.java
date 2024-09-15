package com.bank.transaction_online_banking_service.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountBalanceUpdate {
    private String accountNumber;
    private double accountBalance;

    private int transactionCount;
}