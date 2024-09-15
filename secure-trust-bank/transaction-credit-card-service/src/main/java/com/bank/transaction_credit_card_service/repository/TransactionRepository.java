package com.bank.transaction_credit_card_service.repository;

import com.bank.transaction_credit_card_service.model.CreditCardTransactionDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<CreditCardTransactionDetails,Integer> {
    List<CreditCardTransactionDetails> findAllByCreditCardNumberAndTransactionStatus(String creditCardNumber, String status);
    List<CreditCardTransactionDetails> findAllByCreditCardNumber(String creditCardNumber);
    List<CreditCardTransactionDetails> findByBillNumberAndCreditCardNumber(Integer billNo, String creditCardNumber);
    boolean existsByBillNumberAndCreditCardNumberAndTransactionStatus(int billNo, String creditCardNumber,
                                                                      String transactionStatus);

}