package com.bank.transaction_online_banking_service.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepositRequest {

    @NotNull(message = "deposit must not be null")
    private double deposit;
    private String accountNumber;
}