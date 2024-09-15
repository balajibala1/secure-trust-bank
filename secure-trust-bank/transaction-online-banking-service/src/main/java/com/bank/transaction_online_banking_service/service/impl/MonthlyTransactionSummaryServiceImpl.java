package com.bank.transaction_online_banking_service.service.impl;


import com.bank.transaction_online_banking_service.dto.*;
import com.bank.transaction_online_banking_service.feign.AccountService;
import com.bank.transaction_online_banking_service.model.TransactionDetails;
import com.bank.transaction_online_banking_service.repository.TransactionDetailsRepository;
import com.bank.transaction_online_banking_service.service.TransactionSummaryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class MonthlyTransactionSummaryServiceImpl implements TransactionSummaryService {

    @Autowired
    private AccountService accountService;
    @Autowired
    private TransactionDetailsRepository transactionDetailsRepository;

    @Override
    public SummaryDetails transactionSummary(AccountDetailsRequest userDto) {
        List<TransactionDetailsResponse> transactionDetailsResponseList=new ArrayList<>();
        AccountDetails accountDetails= (AccountDetails) accountService.accountDetailsByAccountNumber(userDto).getBody().getData();
        List<TransactionDetails> transactionDetailsList=transactionDetailsRepository.
                findAllByAccountNumberAndTransactions_TransactionDateBetween
                        (accountDetails.getAccountNumber(), YearMonth.now().atDay(1),YearMonth.now().atEndOfMonth());
        for (TransactionDetails transactionDetails:transactionDetailsList){
            TransactionResponse transactionResponse=new TransactionResponse(transactionDetails.getTransactions().getTransactionId(),
                    transactionDetails.getTransactions().getTransactionDate(), transactionDetails.getTransactions().getTransactionType(),
                    transactionDetails.getTransactions().getTransactionDescription());
            TransactionDetailsResponse transactionDetailsResponse=new TransactionDetailsResponse();
            transactionDetailsResponse.setTransactionDetailsId(transactionDetails.getTransactionDetailsId());
            transactionDetailsResponse.setTransaction(transactionResponse);
            transactionDetailsResponse.setCredited(transactionDetails.getCredited());
            transactionDetailsResponse.setDebited(transactionDetails.getDebited());

            transactionDetailsResponseList.add(transactionDetailsResponse);
        }
        SummaryDetails summaryDetails=new SummaryDetails();
        summaryDetails.setAccountNumber(accountDetails.getAccountNumber());
        summaryDetails.setCurrentBalance(accountDetails.getAccountBalance());
        summaryDetails.setTransactionDetailsList(transactionDetailsResponseList);
        return summaryDetails;
    }
}