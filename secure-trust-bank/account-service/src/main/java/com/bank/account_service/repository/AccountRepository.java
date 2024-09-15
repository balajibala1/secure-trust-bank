package com.bank.account_service.repository;

import com.bank.account_service.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {

    Optional<Account> findByUserId(String userId);

    Account findByUserIdAndAccountNumber(String userId, String accountNumber);

    List<Account> findAllByUserId(String userId);
}
