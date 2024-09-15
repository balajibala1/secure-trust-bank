package com.bank.transaction_online_banking_service.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponse {
    private int transactionId;

    private LocalDate transactionDate;
    private String transactionType;
    private String transactionDescription;


}