package com.bank.user_service.dto;

import jakarta.persistence.Id;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountDto {
    @Id
    private String accountNumber;
    private Integer userId;
    private Double accountBalance;
    private String accountStatus;
    private String kyc;
    private Integer transactionCount;
}
