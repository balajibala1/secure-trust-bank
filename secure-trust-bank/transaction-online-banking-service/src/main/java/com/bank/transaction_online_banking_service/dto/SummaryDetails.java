package com.bank.transaction_online_banking_service.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SummaryDetails {

    private String accountNumber;
    private double currentBalance;
    private List<TransactionDetailsResponse> transactionDetailsList;
}