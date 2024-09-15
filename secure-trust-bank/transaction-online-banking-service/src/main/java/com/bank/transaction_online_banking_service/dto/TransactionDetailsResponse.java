package com.bank.transaction_online_banking_service.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDetailsResponse {
    private int transactionDetailsId;

    private TransactionResponse transaction;


    private double debited;

    private double credited;
}