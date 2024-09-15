package com.bank.transaction_credit_card_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponse {
    private String status;
    private Map<String,Double> data;
    private Map<String,String> error;
}
