package com.bank.transaction_online_banking_service.repository;

import com.bank.transaction_online_banking_service.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

}