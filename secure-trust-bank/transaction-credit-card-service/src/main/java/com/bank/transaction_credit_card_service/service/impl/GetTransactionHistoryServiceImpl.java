package com.bank.transaction_credit_card_service.service.impl;

import com.bank.transaction_credit_card_service.dto.*;
import com.bank.transaction_credit_card_service.exception.CustomException;
import com.bank.transaction_credit_card_service.feign.CreditCardFeignService;
import com.bank.transaction_credit_card_service.model.CreditCardTransactionDetails;
import com.bank.transaction_credit_card_service.repository.TransactionRepository;
import com.bank.transaction_credit_card_service.service.GetTransactionHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GetTransactionHistoryServiceImpl implements GetTransactionHistoryService {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private CreditCardFeignService creditCardService;

    private static final String BILLED = "BILLED";
    private static final String UN_BILLED = "UN_BILLED";
    private static final String PURCHASE = "PURCHASE";
    private static final String PAYMENT = "PAYMENT";

    @Override
    public Map<String, Object> getCurrentMonthTransactions(String creditCardNumber) throws CustomException {
        Map<String, Object> result = new LinkedHashMap<>();
        CreditCardDto creditCardDto= creditCardService.getCreditCardDetails(creditCardNumber).
                orElseThrow(()->new CustomException(creditCardNumber+" Credit Card Number Does Not Exists"));
        result.put("CreditLimit", creditCardDto.getCreditCardLimit());
        result.put("AvailableLimit", creditCardDto.getAvailableLimit());
        result.put("OutStandingAmount", getOutStandingAmount(creditCardNumber));
        List<CreditCardTransactionDetails> transactions = transactionRepository.findAllByCreditCardNumber(creditCardNumber);
        if(!transactions.isEmpty()) {
            List<CreditCardTransactionDetails> unBilledTransactions = transactions.stream()
                    .filter(filter -> filter.getTransactionStatus().contains(UN_BILLED)).toList();
            result.put(UN_BILLED, unBilledTransactions);
            List<CreditCardTransactionDetails> billedTransactions = transactions.stream()
                    .filter(filter -> filter.getTransactionStatus().contentEquals(BILLED)).toList();
            result.put(BILLED, billedTransactions);
            return result;
        }
        throw new CustomException(creditCardNumber+" Don't have any Transactions");
    }
    public double getOutStandingAmount(String cardNumber) throws CustomException {
        List<CreditCardTransactionDetails> transactions = transactionRepository.findAllByCreditCardNumber(cardNumber);
        if(!transactions.isEmpty()) {
            double billedAmount = transactions.stream()
                    .filter(transaction -> transaction.getTransactionStatus().contentEquals(BILLED)
                            && transaction.getTransactionType().contentEquals(PURCHASE))
                    .mapToDouble(CreditCardTransactionDetails::getAmount).sum();
            double advancePaymentAmount = transactions.stream()
                    .filter(transaction -> transaction.getTransactionStatus().contains(BILLED)
                            && transaction.getTransactionType().contentEquals(PAYMENT))
                    .mapToDouble(CreditCardTransactionDetails::getAmount).sum();
            return billedAmount-advancePaymentAmount;
        }
        throw new CustomException(cardNumber+" Don't have any Transactions");
    }
}