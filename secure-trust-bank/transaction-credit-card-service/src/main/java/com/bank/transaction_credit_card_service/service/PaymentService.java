package com.bank.transaction_credit_card_service.service;

import com.bank.transaction_credit_card_service.dto.TransactionDto;
import com.bank.transaction_credit_card_service.exception.CustomException;

import java.util.HashMap;

public interface PaymentService {
    HashMap<String, Double> payment(TransactionDto transactionDetails) throws CustomException;
}
