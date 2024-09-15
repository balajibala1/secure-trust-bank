package com.bank.transaction_online_banking_service.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDetails {

    private String accountNumber;
    private String userId;
    private double accountBalance;
    private String accountStatus;
    private String kyc;

    private int transactionCount;
}