package com.bank.transaction_credit_card_service.service.impl;

import com.bank.transaction_credit_card_service.model.CreditCardTransactionDetails;
import com.bank.transaction_credit_card_service.repository.TransactionRepository;
import com.bank.transaction_credit_card_service.service.GenerateBillService;
import com.bank.transaction_credit_card_service.util.BillNumberGenerator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class GenerateBillServiceImpl implements GenerateBillService {
    private BillNumberGenerator billNumberGenerator;
    private TransactionRepository transactionRepository;

    private static final String UN_BILLED = "UN_BILLED";
    private static final String BILLED = "BILLED";


    @Override
    public String generateBill() {
        getCreditCardNumbers().forEach(creditCardNumber->{
            List<CreditCardTransactionDetails> getUnBilledTransactions = transactionRepository.
                    findAllByCreditCardNumberAndTransactionStatus(creditCardNumber,UN_BILLED);
            Integer billNumber = billNumberGenerator.generateBillNumber();
            getUnBilledTransactions.forEach(unBilledTransactions->{
                unBilledTransactions.setTransactionStatus(BILLED);
                unBilledTransactions.setBillNumber(billNumber);
                unBilledTransactions.setModifiedDate(LocalDate.now());
            });
            transactionRepository.saveAll(getUnBilledTransactions);
        });
        log.info("Bill Generated SuccessFully No Errors in the GenerateBillMethod()");
        return "Bill Generated SuccessFully";
    }
    List<String> getCreditCardNumbers(){
        log.info("Getting all Credit Card Numbers No errors in the getCreditCardNumbers()");
        return transactionRepository.findAll().stream().map(CreditCardTransactionDetails::getCreditCardNumber).distinct().toList();
    }
}