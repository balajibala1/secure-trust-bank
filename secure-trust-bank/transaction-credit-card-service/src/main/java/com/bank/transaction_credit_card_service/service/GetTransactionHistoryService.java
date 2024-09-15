package com.bank.transaction_credit_card_service.service;

import com.bank.transaction_credit_card_service.exception.CustomException;

import java.util.Map;

public interface GetTransactionHistoryService {
    Map<String, Object> getCurrentMonthTransactions(String creditCardNumber)throws CustomException;
}
