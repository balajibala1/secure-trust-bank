package com.bank.transaction_online_banking_service.service;

import com.bank.transaction_online_banking_service.dto.DepositRequest;
import com.bank.transaction_online_banking_service.dto.SummaryDetails;
import com.bank.transaction_online_banking_service.exception.InvalidException;
import org.springframework.stereotype.Service;

@Service
public interface TransactionService {
    SummaryDetails doTransaction(String userId, DepositRequest depositRequest) throws InvalidException;
}
