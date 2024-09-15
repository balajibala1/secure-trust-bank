package com.bank.transaction_online_banking_service.service.impl;

import com.bank.transaction_online_banking_service.dto.*;
import com.bank.transaction_online_banking_service.feign.AccountService;
import com.bank.transaction_online_banking_service.model.ScheduledTransaction;
import com.bank.transaction_online_banking_service.model.Transaction;
import com.bank.transaction_online_banking_service.model.TransactionDetails;
import com.bank.transaction_online_banking_service.repository.*;
import com.bank.transaction_online_banking_service.repository.TransactionDetailsRepository;
import com.bank.transaction_online_banking_service.service.TransactionSummaryService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;


@AllArgsConstructor
@Slf4j
@Service
public class TransactionServices {

    @Autowired
    private ScheduledTransactionRepository scheduleRepo;
    @Autowired
    private TransactionRepository transactionRepo;
    @Autowired
    private TransactionDetailsRepository transactionDetailRepo;
    @Autowired
    private AccountService accountService;
    @Autowired
    private Validator validator;

    Logger logger = LoggerFactory.getLogger(TransactionServices.class);

    public ScheduledTransaction scheduleFundTransfer(ScheduledTransactionDto scheduledTransaction) {
        logger.info("ScheduleFundTransfer method starts");
        String toAccountNumber =scheduledTransaction.getToAccountNumber();
        double amount=scheduledTransaction.getTransferAmount();

        LocalDateTime scheduledDateTime = Validator.parseDateOfBirth(scheduledTransaction.getScheduledOn());
        boolean isValidTime = Validator.isValidDateOfBirth(scheduledDateTime);

        AccountDetails fromAccount = accountService.accountDetailsByAccountNumber(new AccountDetailsRequest(scheduledTransaction.getFromAccountNumber())).getBody().getData();
        AccountDetailsRequest toAccNumDto=new AccountDetailsRequest();
        toAccNumDto.setAccountNumber(toAccountNumber);
        AccountDetails toAccount = accountService.accountDetailsByAccountNumber(toAccNumDto).getBody().getData();
        if(fromAccount==toAccount)
        {
            throw new IllegalArgumentException("FromAccount and ToAccount should not be same.");
        }
        if (!isAccountActive(toAccount)) {
            throw new IllegalArgumentException("The 'to' account is not active.");
        }
        if (checkSufficientBalance(fromAccount,amount)) {
            // Create a new Schedule transaction
            ScheduledTransaction transaction = new ScheduledTransaction();
            transaction.setScheduledOn(scheduledDateTime); // "2023-09-05 15:30:00"
            transaction.setExecuted("pending");
            transaction.setFromAccountNumber(fromAccount.getAccountNumber());
            transaction.setToAccountNumber(toAccountNumber);
            transaction.setTransferAmount(amount);
            scheduleRepo.save(transaction);
            logger.info("ScheduleFundTransfer method ends");
            return transaction;
        } else {
            throw new IllegalArgumentException("Amount should be greater than 999 & multiples of 1000");
        }
    }



    public boolean isAccountActive(AccountDetails accountNumber) {
        String status = accountNumber.getAccountStatus();
        if ("active".equalsIgnoreCase(status)) {
            return true;
        }
        return false;
    }

    public boolean isKycStatusValid(AccountDetails accountNumber) {
        logger.info("isKycStatusValid method starts");
        String kycStatus = accountNumber.getKyc();
        if ("initiated".equalsIgnoreCase(kycStatus) && accountNumber.getTransactionCount()<5)
        {
            logger.info("Account is initiated and transaction count is less than 5");
            logger.info("isKycStatusValid method ends");
            return true; // Allow up to 5 transactions if KYC is initiated
        }
        else if (!kycStatus.equalsIgnoreCase("initiated"))
        {
            logger.info("Account kyc is updated and allowing transactions");
            logger.info("isKycStatusValid method ends");
            return true; // Allow transactions if KYC is updated or any other valid status
        }
        logger.info("isKycStatusValid method ends");
        return false;
    }

    public boolean checkSufficientBalance(AccountDetails account, double amount) {
        return account.getAccountBalance()>=amount && amount > 999 && amount % 1000 == 0;
    }

    @Transactional
    public void performTransfer(AccountDetails fromAccount, AccountDetails toAccount, Double amount,ScheduledTransaction transfer) {
        logger.info("performTransfer method starts");
        Double updatedFromAccountBalance=fromAccount.getAccountBalance() - amount;
        logger.info("updatedFromAccountBalance: "+updatedFromAccountBalance);
        Double updatedToAccountBalance=toAccount.getAccountBalance() + amount;
        logger.info("updatedToAccountBalance: "+updatedToAccountBalance);
        Transaction transaction = new Transaction();
        transaction.setTransactionDate(LocalDate.now());
        transaction.setTransactionType("S");
        transaction.setTransactionDescription("Scheduled transfer done from "+fromAccount.getAccountNumber()+" to"+toAccount.getAccountNumber());
        transactionRepo.save(transaction);

        TransactionDetails debitDetail = new TransactionDetails();
        debitDetail.setTransactions(transaction);
        debitDetail.setAccountNumber(fromAccount.getAccountNumber());
        debitDetail.setDebited(amount);
        debitDetail.setCredited(0.0);

        // Create a new transaction detail record for 'toAccount' (credit)
        TransactionDetails creditDetail = new TransactionDetails();
        creditDetail.setTransactions(transaction);
        creditDetail.setAccountNumber(toAccount.getAccountNumber());
        creditDetail.setCredited(amount);
        creditDetail.setDebited(0.0);

        // Save the debit and credit details
        transactionDetailRepo.save(debitDetail);
        transactionDetailRepo.save(creditDetail);

        transfer.setTransactionId(transaction.getTransactionId());
        transfer.setExecuted("processed");
        scheduleRepo.save(transfer);

        int transactionCount = fromAccount.getTransactionCount();
        if(fromAccount.getKyc().equalsIgnoreCase("initiated") && fromAccount.getTransactionCount()<5){
            transactionCount++;
        }
        else{
            transactionCount=0;
        }
        fromAccount.setAccountBalance(fromAccount.getAccountBalance() - amount);
        fromAccount.setTransactionCount(transactionCount);
        toAccount.setAccountBalance(toAccount.getAccountBalance() + amount);
        toAccount.setTransactionCount(0);

        AccountBalanceUpdate fromAcc = new AccountBalanceUpdate();
        fromAcc.setAccountNumber(fromAccount.getAccountNumber());
        fromAcc.setAccountBalance(updatedFromAccountBalance);
        fromAcc.setTransactionCount(transactionCount);
        AccountBalanceUpdate toAcc = new AccountBalanceUpdate();
        toAcc.setAccountNumber(toAccount.getAccountNumber());
        toAcc.setAccountBalance(updatedToAccountBalance);
        toAcc.setTransactionCount(toAccount.getTransactionCount());

        accountService.updateAccountBalance(fromAcc);
        accountService.updateAccountBalance(toAcc);
        logger.info("performTransfer method ends");
    }

}