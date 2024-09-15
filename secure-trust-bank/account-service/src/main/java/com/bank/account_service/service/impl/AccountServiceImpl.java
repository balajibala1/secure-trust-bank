package com.bank.account_service.service.impl;

import com.bank.account_service.dto.AccountKycDto;
import com.bank.account_service.dto.*;
import com.bank.account_service.dto.AccountTypeDto;
import com.bank.account_service.exception.AccountNotPresentException;
import com.bank.account_service.exception.AlreadyAppliedException;
import com.bank.account_service.model.Account;
import com.bank.account_service.model.ProspectApplication;
import com.bank.account_service.repository.*;
import com.bank.account_service.service.AccountService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import com.bank.authorization.security.config.*;
@Service
@Slf4j
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private static final String PREFIX = "ABC";
    private static final int NUM_DIGITS = 10;
    private final AtomicInteger accountNumberCounter = new AtomicInteger(1);

    @Autowired
    AccountRepository accountRepository;
    @NonNull
    private ProspectRepository prospectRepository;
    @Autowired
    ValidateToken jwtFilter;
    @Value("${error.application.code}")
    private String code;
    @Override
    public String applyOnlineBanking(AccountTypeDto accountTypeDto, String token) throws AlreadyAppliedException {
        String userId = jwtFilter.extractUserName(token);
        log.info(userId);
        ProspectApplication alreadyRegisteredApplication = prospectRepository.findByUserAndTypeAndAccountType(userId, "Online-Banking", accountTypeDto.getAccountType());
        ProspectApplication prospectApplication1 = new ProspectApplication();
        if (alreadyRegisteredApplication == null) {
            prospectApplication1.setStatus("PENDING");
            prospectApplication1.setCreatedName("ADMIN");
            prospectApplication1.setCreatedDate(LocalDate.now());
            prospectApplication1.setType("Online-Banking");
            prospectApplication1.setUser(userId);
            prospectApplication1.setAccountType(accountTypeDto.getAccountType());
            prospectRepository.save(prospectApplication1);
            return "Success";
        } else {
            throw new AlreadyAppliedException(code);
        }
    }
    @Override
    public AccountKycDto initiateKyc(AccountNumberDto accountNumberDto) {
        log.info("AccountServiceImpl: initiateKyc(-); Starts]");
        Account account = accountRepository.findById(accountNumberDto.getAccountNumber()).get();
        account.setKyc("INITIATED");
        accountRepository.save(account);
        log.info("AccountServiceImpl: initiateKyc(-); Ends]");
        AccountKycDto accountKycDto = new AccountKycDto(account.getAccountNumber(),account.getKyc());
        return accountKycDto;
    }

    @Override
    public AccountKycDto updateKyc(String token) {
        log.info("AccountServiceImpl: updateKyc(-); Starts]");
        String userId=jwtFilter.extractUserName(token);
        Account account=accountRepository.findByUserId(userId).get();
        account.setKyc("UPDATED");
        account.setTransactionCount(0);
        accountRepository.save(account);
        log.info("AccountServiceImpl: updateKyc(-); ends]");
        AccountKycDto accountKycDto = new AccountKycDto(account.getAccountNumber(),account.getKyc());
        return accountKycDto;
    }
    @Override
    public List<AccountDto> getAccountDetails(Integer userId) throws AccountNotPresentException {
//		Account account =accountRepository.findByUserIdAndAccountNumber(userIdDto.getUserId(), userIdDto.getAccountNumber());
//		if(account==null){
//			throw new AccountNotPresentException("Account Not Found");
//		}
//		AccountDto accountDto = new AccountDto().builder().accountNumber(account.getAccountNumber())
//				.userId(account.getUserId()).accountStatus(account.getAccountStatus()).accountBalance(account.getAccountBalance())
//				.kyc(account.getKyc()).transactionCount(account.getTransactionCount()).build();
//		log.info(accountDto.toString());
//		return accountDto;

        List<Account> accounts=accountRepository.findAllByUserId(userId.toString());
        List<AccountDto> accountResponse=accounts.stream().map( account -> {
            AccountDto acc=new AccountDto();
            acc.setAccountNumber(account.getAccountNumber());
            acc.setAccountStatus(account.getAccountStatus());
            acc.setAccountBalance(account.getAccountBalance());
            acc.setKyc(account.getKyc());
            acc.setTransactionCount(account.getTransactionCount());
            acc.setUserId(account.getUserId());
            return acc;
        }).toList();
        return accountResponse;
    }



    @Override
    public Account getAccountDetailsByAccountNumber(AccountNumberDto accountNumberDto) throws AccountNotPresentException {
        return accountRepository.findById(accountNumberDto.getAccountNumber()).orElseThrow(()->new AccountNotPresentException("Account Not Found"));
    }

    private static String generateAccountNumber() {
        // Generate lowercase UUID
        String lowercaseUUID = UUID.randomUUID().toString().replace("-", "").toLowerCase();

        // Extract the first N digits from the UUID
        String numericPart = extractNumericPart(lowercaseUUID, NUM_DIGITS);

        // Combine the parts
        return PREFIX + numericPart;
    }

    private static String extractNumericPart(String input, int numDigits) {
        StringBuilder numericPart = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (Character.isDigit(c)) {
                numericPart.append(c);
                if (numericPart.length() == numDigits) {
                    break;
                }
            }
        }
        return numericPart.toString();
    }

    @Override
    public Account createAccount(UserIdDto userIdDto) {
        log.info("AccountServiceImpl: createAccount(-); Starts]");
        Account account=new Account();
        account.setAccountNumber(generateAccountNumber());
        account.setUserId(userIdDto.getUserId());
        account.setAccountBalance(0.0);
        account.setAccountStatus("ACTIVE");
        account.setKyc("UPDATED");
        account.setTransactionCount(0);
        account.setAccountType(userIdDto.getType());
        log.info("AccountServiceImpl: createAccount(-); ends]");
        return accountRepository.save(account);
    }

    @Override
    public Account updateAccountBalanceAndTransactionCount(AccountBalanceWithTransactionCountDto accountBalanceWithTransactionCountDto){
        Account account=accountRepository.findById(accountBalanceWithTransactionCountDto.getAccountNumber()).get();
        account.setAccountBalance(accountBalanceWithTransactionCountDto.getAccountBalance());
        account.setTransactionCount(accountBalanceWithTransactionCountDto.getTransactionCount());
        return accountRepository.save(account);
    }

    @Override
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public Account getAccountByToken(String userId) {
        return accountRepository.findByUserId(userId).get();
    }

    @Override
    public List<GetAllAccountNumberAndType> getAllAccountsByUserID(String substring) throws AccountNotPresentException {

        List<Account> accounts =  accountRepository.findAllByUserId(substring);
        List<GetAllAccountNumberAndType> getAllAccountNumberAndTypes = accounts.stream().map(
                i->new GetAllAccountNumberAndType(i.getAccountNumber(),i.getAccountType(),i.getAccountBalance(),i.getKyc())
        ).toList();
        return getAllAccountNumberAndTypes;
    }

}