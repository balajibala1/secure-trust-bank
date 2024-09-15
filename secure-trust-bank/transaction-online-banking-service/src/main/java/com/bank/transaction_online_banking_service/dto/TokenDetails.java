package com.bank.transaction_online_banking_service.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenDetails {
    private String accountNumber;

    private String accountStatus;
    private String role;
}