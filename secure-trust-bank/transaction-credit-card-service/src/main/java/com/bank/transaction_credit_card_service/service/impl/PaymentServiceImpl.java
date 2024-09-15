package com.bank.transaction_credit_card_service.service.impl;

import com.bank.transaction_credit_card_service.dto.CreditCardDto;
import com.bank.transaction_credit_card_service.dto.TransactionDto;
import com.bank.transaction_credit_card_service.exception.CustomException;
import com.bank.transaction_credit_card_service.feign.CreditCardFeignService;
import com.bank.transaction_credit_card_service.model.CreditCardTransactionDetails;
import com.bank.transaction_credit_card_service.repository.TransactionRepository;
import com.bank.transaction_credit_card_service.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    private CreditCardFeignService creditCardFeignService;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private GetTransactionHistoryServiceImpl getTransactionHistoryService;

    private static final String PAID = "PAID";
    private static final String BILLED = "BILLED";
    private static final String UN_BILLED = "UN_BILLED";
    private static final String PAYMENT = "PAYMENT";


    @Override
    public HashMap<String, Double> payment(TransactionDto transactionDetails)
            throws CustomException {
        int billNo = transactionDetails.getBillNo();
        String creditCardNumber = transactionDetails.getCreditCardNumber();
        double paymentAmount = transactionDetails.getAmount();
        double outStandingAmount = getTransactionHistoryService.getOutStandingAmount(creditCardNumber);
        HashMap<String, Double> data = new HashMap<>();
        CreditCardTransactionDetails transaction = new CreditCardTransactionDetails();
        transaction.setTransactionDate(LocalDate.now());
        transaction.setTransactionType(PAYMENT);
        transaction.setAmount(paymentAmount);
        transaction.setModifiedDate(LocalDate.now());
        transaction.setCreditCardNumber(creditCardNumber);
        CreditCardDto creditCard = creditCardFeignService.getCreditCardDetails(creditCardNumber)
                .orElseThrow(() -> new CustomException("Credit Card doesn't exist in the database"));
        log.error("Credit Card doesn't exist in the database in Payment() Method");
        List<CreditCardTransactionDetails> transactionsFromBillNo = transactionRepository.findByBillNumberAndCreditCardNumber(billNo,
                creditCardNumber);
        boolean paidTransaction = transactionRepository
                .existsByBillNumberAndCreditCardNumberAndTransactionStatus(billNo, creditCardNumber, PAID);
        if (billNo == 0) {
            transaction.setTransactionStatus(UN_BILLED);
            transaction.setCreatedDate(LocalDate.now());
        }
        else if (billNo > 0 && transactionsFromBillNo.isEmpty()) {
            log.error("Invalid Bill Number in Payment() Method");
            throw new CustomException("Invalid Bill Number");
        } else if (billNo > 0 && (paidTransaction || outStandingAmount <= 0)) {
            log.error("Paid Already in Payment() Method");
            throw new CustomException("Paid Already");
        }
        else if (billNo > 0 && outStandingAmount != paymentAmount) {
            log.error("Invalid Amount, Your Current Outstanding Amount is: "+outStandingAmount+ "in Payment() Method");
            throw new CustomException("Invalid Amount, Your Current Outstanding Amount is: " + outStandingAmount);
        }
        else if (billNo > 0 && outStandingAmount == paymentAmount) {
            List<CreditCardTransactionDetails> cardPurchaseTransactions = transactionRepository.findAllByCreditCardNumber(creditCardNumber)
                    .stream().filter(filter -> filter.getTransactionType().contentEquals("PURCHASE") &&
                            filter.getTransactionStatus().contentEquals(BILLED)).toList();
            cardPurchaseTransactions.forEach(cardTransact -> {
                cardTransact.setTransactionStatus(PAID);
                cardTransact.setBillNumber(billNo);
                log.info("Created / Modified Bill No to "+ billNo);
                cardTransact.setModifiedDate(LocalDate.now());
                transactionRepository.save(cardTransact);
            });
            List<CreditCardTransactionDetails> cardPaymentTransactions = transactionRepository.findAllByCreditCardNumber(creditCardNumber)
                    .stream().filter(filter -> filter.getTransactionType().contentEquals(PAYMENT) &&
                            filter.getTransactionStatus().contains(BILLED)).toList();
            cardPaymentTransactions.forEach(cardTransact -> {
                cardTransact.setTransactionStatus(PAID);
                cardTransact.setBillNumber(billNo);
                cardTransact.setModifiedDate(LocalDate.now());
                transactionRepository.save(cardTransact);
            });
            transaction.setBillNumber(billNo);
            transaction.setTransactionStatus(PAID);
            transaction.setCreatedDate(LocalDate.now());
        }
        transactionRepository.save(transaction);
        double updatedAvailableLimit = creditCard.getAvailableLimit() + paymentAmount;
        creditCard.setAvailableLimit(updatedAvailableLimit);
        creditCardFeignService.updateAvailableLimit(creditCard);
        data.put("AmountPaid", transactionDetails.getAmount());
        data.put("AvailableLimit", updatedAvailableLimit);
        log.info("No errors in Payment() Method");
        return data;
    }
}
