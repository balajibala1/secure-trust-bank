package com.bank.transaction_online_banking_service.service.impl;

import com.bank.transaction_online_banking_service.dto.DepositRequest;
import com.bank.transaction_online_banking_service.dto.*;
import com.bank.transaction_online_banking_service.exception.InvalidException;
import com.bank.transaction_online_banking_service.feign.AccountService;
import com.bank.transaction_online_banking_service.model.Transaction;
import com.bank.transaction_online_banking_service.model.TransactionDetails;
import com.bank.transaction_online_banking_service.repository.TransactionDetailsRepository;
import com.bank.transaction_online_banking_service.repository.TransactionRepository;
import com.bank.transaction_online_banking_service.service.TransactionService;
import groovy.util.logging.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class DepositTransactionServiceImpl implements TransactionService {

    @Value(value = "${account.transaction-count.limit}")
    int transactionCountLimit;
    @Value(value="${account.kyc.initiated}")
    String kycInitiated;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionDetailsRepository transactionDetailsRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public SummaryDetails doTransaction(String userId, DepositRequest depositRequest) throws InvalidException {
        AccountDetails updatedAccountDetails;
        TransactionDetails updatedTransactionDetails;
        //AccountDetailsRequest accountDetailsRequest = new AccountDetailsRequest(userId);
        AccountDetailsRequest accountDetailsRequest = new AccountDetailsRequest(depositRequest.getAccountNumber());
        AccountDetails accountDetails = accountService.accountDetailsByAccountNumber(accountDetailsRequest).getBody().getData();
        if (accountDetails.getKyc().equals(kycInitiated) && accountDetails.getTransactionCount() == transactionCountLimit) {
            throw new InvalidException("The Account has reached the limit of transactions, update the KYC");
        } else if (depositRequest.getDeposit() <= 0) {
            throw new InvalidException("Deposit Amount cannot be Negative or Zero");
        } else {
            Transaction transaction = new Transaction();
            transaction.setTransactionDate(LocalDate.now());
            transaction.setTransactionType("D");
            transaction.setTransactionDescription("Deposit Executed");
            Transaction transactionValue = transactionRepository.save(transaction);
            TransactionDetails transactionDetails = new TransactionDetails();
            transactionDetails.setAccountNumber(depositRequest.getAccountNumber());
            transactionDetails.setTransactions(transactionValue);
            transactionDetails.setCredited(depositRequest.getDeposit());
            transactionDetails.setDebited(0.0);
            updatedTransactionDetails = transactionDetailsRepository.save(transactionDetails);
            int transactionCount = accountDetails.getTransactionCount();
            if(accountDetails.getKyc().equals(kycInitiated)){
                transactionCount++;
            }
            else{
                transactionCount=0;
            }
            double updatedBalance = accountDetails.getAccountBalance() + depositRequest.getDeposit();
            AccountBalanceUpdate accountBalanceUpdate = new AccountBalanceUpdate(accountDetails.getAccountNumber(), updatedBalance, transactionCount);
            updatedAccountDetails = accountService.updateAccountBalance(accountBalanceUpdate).getBody().getData();
        }
        System.out.println(updatedAccountDetails);
        return summaryDetails(updatedAccountDetails, updatedTransactionDetails);
    }

    private SummaryDetails summaryDetails(AccountDetails updatedAccountDetails, TransactionDetails updatedTransactionDetails) {
        TransactionResponse transactionResponse = new TransactionResponse();
        transactionResponse.setTransactionId(updatedTransactionDetails.getTransactions().getTransactionId());
        transactionResponse.setTransactionDate(updatedTransactionDetails.getTransactions().getTransactionDate());
        transactionResponse.setTransactionType(updatedTransactionDetails.getTransactions().getTransactionType());
        transactionResponse.setTransactionDescription(updatedTransactionDetails.getTransactions().getTransactionDescription());

        TransactionDetailsResponse transactionDetailsResponse = new TransactionDetailsResponse();
        transactionDetailsResponse.setTransactionDetailsId(updatedTransactionDetails.getTransactionDetailsId());
        transactionDetailsResponse.setTransaction(transactionResponse);
        transactionDetailsResponse.setDebited(updatedTransactionDetails.getDebited());
        transactionDetailsResponse.setCredited(updatedTransactionDetails.getCredited());
        SummaryDetails summaryDetails = new SummaryDetails();
        summaryDetails.setAccountNumber(updatedAccountDetails.getAccountNumber());
        summaryDetails.setCurrentBalance(updatedAccountDetails.getAccountBalance());
        summaryDetails.setTransactionDetailsList(List.of(transactionDetailsResponse));
        return summaryDetails;
    }
}