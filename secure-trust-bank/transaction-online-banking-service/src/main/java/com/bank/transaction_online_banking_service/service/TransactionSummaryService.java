package com.bank.transaction_online_banking_service.service;

import com.bank.transaction_online_banking_service.dto.AccountDetailsRequest;
import com.bank.transaction_online_banking_service.dto.SummaryDetails;


public interface TransactionSummaryService {
    SummaryDetails transactionSummary(AccountDetailsRequest userDto);

    //SummaryDetails transactionSummary(UserDto userDto);
}