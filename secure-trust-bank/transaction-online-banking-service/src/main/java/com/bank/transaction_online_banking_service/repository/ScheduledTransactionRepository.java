package com.bank.transaction_online_banking_service.repository;


import com.bank.transaction_online_banking_service.model.ScheduledTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduledTransactionRepository extends JpaRepository<ScheduledTransaction, Integer> {
    List<ScheduledTransaction> findByScheduledOnBeforeAndExecuted(LocalDateTime scheduledDateTime, String executed);

    ScheduledTransaction findByFromAccountNumber(String accountNumber);

}