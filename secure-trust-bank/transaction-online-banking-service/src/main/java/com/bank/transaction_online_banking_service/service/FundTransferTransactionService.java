package com.bank.transaction_online_banking_service.service;

import com.bank.transaction_online_banking_service.dto.FundTransfer;
import com.bank.transaction_online_banking_service.dto.SummaryDetails;
import com.bank.transaction_online_banking_service.exception.InvalidException;


public interface FundTransferTransactionService {
    SummaryDetails doFundTransaction(FundTransfer fundTransfer, String userId) throws InvalidException;

}

