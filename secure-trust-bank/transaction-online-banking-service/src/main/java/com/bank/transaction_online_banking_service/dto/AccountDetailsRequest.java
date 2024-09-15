package com.bank.transaction_online_banking_service.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDetailsRequest {
    private String accountNumber;
}