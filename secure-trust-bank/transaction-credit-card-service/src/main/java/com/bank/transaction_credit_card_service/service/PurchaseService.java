package com.bank.transaction_credit_card_service.service;

import com.bank.transaction_credit_card_service.dto.TransactionDto;
import com.bank.transaction_credit_card_service.exception.CustomException;

import java.util.HashMap;

public interface PurchaseService {
    HashMap<String, Double> purchase(TransactionDto transactionDetails) throws CustomException;
}
