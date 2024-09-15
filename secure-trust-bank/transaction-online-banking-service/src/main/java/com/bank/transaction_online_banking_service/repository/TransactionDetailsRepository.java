package com.bank.transaction_online_banking_service.repository;

import com.bank.transaction_online_banking_service.model.TransactionDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TransactionDetailsRepository extends JpaRepository<TransactionDetails, Integer> {
    List<TransactionDetails> findAllByAccountNumberAndTransactions_TransactionDateBetween(
            String accountNumber, LocalDate atDay, LocalDate atEndOfMonth);
}