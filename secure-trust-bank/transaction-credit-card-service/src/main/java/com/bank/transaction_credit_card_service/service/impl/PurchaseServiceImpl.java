package com.bank.transaction_credit_card_service.service.impl;

import com.bank.transaction_credit_card_service.dto.*;
import com.bank.transaction_credit_card_service.exception.CustomException;
import com.bank.transaction_credit_card_service.feign.CreditCardFeignService;
import com.bank.transaction_credit_card_service.model.CreditCardTransactionDetails;
import com.bank.transaction_credit_card_service.repository.TransactionRepository;
import com.bank.transaction_credit_card_service.service.PurchaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;

@Service
@Slf4j
public class PurchaseServiceImpl implements PurchaseService {
    @Autowired
    private CreditCardFeignService creditCardService;

    @Autowired
    private TransactionRepository transactionRepository;
    private static final String UN_BILLED = "UN_BILLED";
    private static final String PURCHASE = "PURCHASE";

    @Override
    public HashMap<String, Double> purchase(TransactionDto transactionDetails) throws CustomException {
        String creditCardNum = transactionDetails.getCreditCardNumber();
        CreditCardDto card = creditCardService.getCreditCardDetails(creditCardNum)
                .orElseThrow(() -> new CustomException("Credit Card doesn't exist in the database"));
        HashMap<String, Double> data = new HashMap<>();
        if (transactionDetails.getAmount() > card.getAvailableLimit()) {
            log.error("Purchase Amount is more than Available limit: " + card.getAvailableLimit()+" in purchase() Method");
            throw new CustomException("Purchase Amount is more than Available limit: " + card.getAvailableLimit());
        }
        else if (transactionDetails.getAmount() <= 0) {
            log.error("Purchase Amount should be greater than 0");
            throw new CustomException("Purchase Amount should be greater than 0");
        }

        else if (transactionDetails.getAmount() > 0 && transactionDetails.getAmount() <= card.getAvailableLimit()) {
            double updatedAvailableLimit = card.getAvailableLimit() - transactionDetails.getAmount();
            card.setAvailableLimit(updatedAvailableLimit);
            CreditCardTransactionDetails transaction = new CreditCardTransactionDetails();
            transaction.setTransactionStatus(UN_BILLED);
            transaction.setTransactionDate(LocalDate.now());
            transaction.setAmount(transactionDetails.getAmount());
            transaction.setCreditCardNumber(card.getCreditCardNumber());
            transaction.setTransactionType(PURCHASE);
            transaction.setCreatedDate(LocalDate.now());
            transaction.setModifiedDate(LocalDate.now());
            creditCardService.updateAvailableLimit(card);
            transactionRepository.save(transaction);
            data.put("Purchase Amount", transactionDetails.getAmount());
            data.put("Available Limit", updatedAvailableLimit);
        }
        log.info("No errors in the Purchase() Method");
        return data;
    }
}
