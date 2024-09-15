package com.bank.transaction_online_banking_service.service.impl;

import com.bank.transaction_online_banking_service.dto.FundTransfer;
import com.bank.transaction_online_banking_service.dto.SummaryDetails;
import com.bank.transaction_online_banking_service.dto.*;
import com.bank.transaction_online_banking_service.exception.InvalidException;
import com.bank.transaction_online_banking_service.feign.AccountService;
import com.bank.transaction_online_banking_service.model.Transaction;
import com.bank.transaction_online_banking_service.model.TransactionDetails;
import com.bank.transaction_online_banking_service.repository.TransactionDetailsRepository;
import com.bank.transaction_online_banking_service.repository.TransactionRepository;
import com.bank.transaction_online_banking_service.service.FundTransferTransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ImmediateFundTransferTransactionServiceImpl implements FundTransferTransactionService {
    @Value(value="${account.kyc.initiated}")
    String kycInitiated;
    @Value("${account.transaction-count.limit}")
    int transactionCountLimit;
    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionDetailsRepository transactionDetailsRepository;
    Logger logger = LoggerFactory.getLogger(ImmediateFundTransferTransactionServiceImpl.class);

    @Override
    public SummaryDetails doFundTransaction(FundTransfer fundTransfer, String userId) throws InvalidException {

        logger.info("Transaction Immediate Service: Immediate Fund Transfer Transaction(-); Starts]");

        TransactionDetails updatedTransactionDetailsFromAccountNumber;
        logger.info(fundTransfer.getFromAccountNumber());
        logger.info(userId);
        UserDto userDto = new UserDto(userId);
        logger.info(userDto.toString());
        AccountDetailsRequest accountDetailsRequest = new AccountDetailsRequest(fundTransfer.getFromAccountNumber());
        ResponseEntity<ApiResponse<AccountDetails>> accountDetailsFromAccountNumber=
                accountService.accountDetailsByAccountNumber(accountDetailsRequest);
        AccountDetailsRequest accountDetailsRequestToAccountNumber=new AccountDetailsRequest
                (fundTransfer.getToAccountNumber());

        AccountDetails accountDetailsToAccountNumber=
                accountService.accountDetailsByAccountNumber(accountDetailsRequestToAccountNumber).getBody().getData();

        if(fundTransfer.getTransferAmount()>accountDetailsFromAccountNumber.getBody().getData().getAccountBalance()){
            throw new InvalidException("TransferAmount is greater than the Account Balance");
        }
        else if (fundTransfer.getTransferAmount()<999 || fundTransfer.getTransferAmount()%1000 != 0) {
            throw new InvalidException("Transfer Amount should greater than 999 and in the multiples of 1000.");
        }
        else if(accountDetailsFromAccountNumber.getBody().getData().getKyc().equals(kycInitiated) &&
                accountDetailsFromAccountNumber.getBody().getData().getTransactionCount()==transactionCountLimit){
            throw new InvalidException("The Account has reached the limit of transactions, update the KYC");
        } else if (accountDetailsToAccountNumber.getAccountStatus().equals("INACTIVE") ||
                accountDetailsFromAccountNumber.getBody().getData().getAccountStatus().equals("INACTIVE")){
            throw new InvalidException("The AccountNumber Provided might be Inactive, contact the helpline!");
        }
        else{
            Transaction transaction=new Transaction();
            transaction.setTransactionDate(LocalDate.now());
            transaction.setTransactionType("F");
            transaction.setTransactionDescription("Fund Transfer of "+ fundTransfer.getTransferAmount()+" Executed to "
                    + accountDetailsToAccountNumber.getAccountNumber());
            Transaction transactionValue=transactionRepository.save(transaction);

            TransactionDetails transactionDetailsFromAccountNumber=new TransactionDetails();
            transactionDetailsFromAccountNumber.setAccountNumber(accountDetailsFromAccountNumber.getBody().getData().getAccountNumber());
            transactionDetailsFromAccountNumber.setTransactions(transactionValue);
            transactionDetailsFromAccountNumber.setCredited(0.0);
            transactionDetailsFromAccountNumber.setDebited(fundTransfer.getTransferAmount());
            updatedTransactionDetailsFromAccountNumber=transactionDetailsRepository.save(transactionDetailsFromAccountNumber);

            TransactionDetails transactionDetailsToAccountNumber=new TransactionDetails();
            transactionDetailsToAccountNumber.setAccountNumber(fundTransfer.getToAccountNumber());
            transactionDetailsToAccountNumber.setTransactions(transactionValue);
            transactionDetailsToAccountNumber.setCredited(fundTransfer.getTransferAmount());
            transactionDetailsToAccountNumber.setDebited(0.0);
            TransactionDetails updatedTransactionDetailsToAccountNumber=transactionDetailsRepository.save(transactionDetailsToAccountNumber);

            int transactionCount=accountDetailsFromAccountNumber.getBody().getData().getTransactionCount();
            if(accountDetailsFromAccountNumber.getBody().getData().getKyc().equals("INITIATED")){
                transactionCount++;
            }
            else {
                transactionCount=0;
            }
            double updatedBalanceFromAccountNumber= accountDetailsFromAccountNumber.getBody().getData().getAccountBalance()- fundTransfer.getTransferAmount();
            AccountBalanceUpdate accountBalanceUpdate=new AccountBalanceUpdate(accountDetailsFromAccountNumber.getBody().getData().getAccountNumber(),updatedBalanceFromAccountNumber,transactionCount);
            AccountDetails updatedAccountDetailsFromAccountNumber =accountService.updateAccountBalance(accountBalanceUpdate).getBody().getData();

            double updatedBalanceToAccountNumber=accountDetailsToAccountNumber.getAccountBalance()+ fundTransfer.getTransferAmount();
            accountBalanceUpdate =new AccountBalanceUpdate(accountDetailsToAccountNumber.getAccountNumber(),updatedBalanceToAccountNumber,accountDetailsToAccountNumber.getTransactionCount());
            AccountDetails updatedAccountDetailsToAccountNumber=accountService.updateAccountBalance(accountBalanceUpdate).getBody().getData();

            return summaryDetails(updatedAccountDetailsFromAccountNumber,updatedTransactionDetailsFromAccountNumber);

        }

    }

    private SummaryDetails summaryDetails(AccountDetails updatedAccountDetailsFromAccountNumber, TransactionDetails updatedTransactionDetailsFromAccountNumber) {
        TransactionResponse transactionResponse=new TransactionResponse();
        transactionResponse.setTransactionId(updatedTransactionDetailsFromAccountNumber.getTransactions().getTransactionId());
        transactionResponse.setTransactionDate(updatedTransactionDetailsFromAccountNumber.getTransactions().getTransactionDate());
        transactionResponse.setTransactionType(updatedTransactionDetailsFromAccountNumber.getTransactions().getTransactionType());
        transactionResponse.setTransactionDescription(updatedTransactionDetailsFromAccountNumber.getTransactions().getTransactionDescription());
        TransactionDetailsResponse transactionDetailsResponse=new TransactionDetailsResponse();
        transactionDetailsResponse.setTransactionDetailsId(updatedTransactionDetailsFromAccountNumber.getTransactionDetailsId());
        transactionDetailsResponse.setTransaction(transactionResponse);
        transactionDetailsResponse.setDebited(updatedTransactionDetailsFromAccountNumber.getDebited());
        transactionDetailsResponse.setCredited(updatedTransactionDetailsFromAccountNumber.getCredited());
        SummaryDetails summaryDetails =new SummaryDetails();
        summaryDetails.setAccountNumber(updatedAccountDetailsFromAccountNumber.getAccountNumber());
        summaryDetails.setCurrentBalance(updatedAccountDetailsFromAccountNumber.getAccountBalance());
        summaryDetails.setTransactionDetailsList(List.of(transactionDetailsResponse));
        return summaryDetails;
    }
}