package com.bank.account_service.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetAllAccountNumberAndType {
    private String accountNumber;
    private String accountType;
    private Double accountBalance;
    private String kyc;
}

