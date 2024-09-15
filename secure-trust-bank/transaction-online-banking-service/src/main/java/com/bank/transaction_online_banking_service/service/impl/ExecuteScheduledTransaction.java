package com.bank.transaction_online_banking_service.service.impl;

import com.bank.transaction_online_banking_service.dto.AccountDetails;
import com.bank.transaction_online_banking_service.dto.AccountDetailsRequest;
import com.bank.transaction_online_banking_service.feign.AccountService;
import com.bank.transaction_online_banking_service.model.ScheduledTransaction;
import com.bank.transaction_online_banking_service.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class ExecuteScheduledTransaction {

    @Autowired
    private ScheduledTransactionRepository scheduleRepo;
    @Autowired
    private TransactionRepository transactionRepo;
    @Autowired
    private TransactionDetailsRepository transactionDetailRepo;
    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionServices transactionServices;

    @Autowired
    private Validator validator;
    Logger logger = LoggerFactory.getLogger(ExecuteScheduledTransaction.class);

    @Scheduled(cron = "0 */2 * * * *")
    public void executeScheduledTransfers() throws ExecutionException, InterruptedException {
        List<ScheduledTransaction> pendingTransactions = scheduleRepo
                .findByScheduledOnBeforeAndExecuted(LocalDateTime.now(), "pending");
        log.info("Scheduled Pending Transaction:Proccess Initiated");
        logger.info(pendingTransactions.toString());
        for (ScheduledTransaction transfer : pendingTransactions) {
            logger.info(transfer.getFromAccountNumber());
            AccountDetails fromAccount = accountService.accountDetailsByAccountNumber(new AccountDetailsRequest(transfer.getFromAccountNumber())).getBody().getData();
            logger.info(fromAccount.toString());
            AccountDetailsRequest toAccNumDto = new AccountDetailsRequest();
            toAccNumDto.setAccountNumber(transfer.getToAccountNumber());
            AccountDetails toAccount = accountService.accountDetailsByAccountNumber(toAccNumDto).getBody().getData();
            if (transactionServices.isAccountActive(fromAccount) && transactionServices.isAccountActive(toAccount) &&
                    transactionServices.isKycStatusValid(fromAccount) &&
                    transactionServices.checkSufficientBalance(fromAccount, transfer.getTransferAmount())) {
                transactionServices.performTransfer(fromAccount, toAccount, transfer.getTransferAmount(), transfer);
            }
        }
        logger.info("Scheduled Pending Transaction:Proccess Exceuted");
    }
}