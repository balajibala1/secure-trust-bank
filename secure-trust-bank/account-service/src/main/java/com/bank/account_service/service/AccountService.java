package com.bank.account_service.service;

import com.bank.account_service.dto.*;
import com.bank.account_service.exception.AccountNotPresentException;
import com.bank.account_service.exception.AlreadyAppliedException;
import com.bank.account_service.model.Account;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AccountService {
    public Account createAccount(UserIdDto userIdDto);
    String applyOnlineBanking(AccountTypeDto accountTypeDto, String token) throws AlreadyAppliedException;
    public AccountKycDto initiateKyc(AccountNumberDto accountNumberDto);
    public AccountKycDto updateKyc(String token);
    public List<AccountDto> getAccountDetails(Integer userId) throws AccountNotPresentException;
    public Account getAccountDetailsByAccountNumber(AccountNumberDto accountNumberDto) throws AccountNotPresentException;
    public Account updateAccountBalanceAndTransactionCount(AccountBalanceWithTransactionCountDto accountBalanceWithTransactionCountDto);

    public List<Account> getAllAccounts();

    public Account getAccountByToken(String userId);

    List<GetAllAccountNumberAndType> getAllAccountsByUserID(String substring) throws AccountNotPresentException;
}
