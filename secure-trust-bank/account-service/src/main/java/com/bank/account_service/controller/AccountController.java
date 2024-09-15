package com.bank.account_service.controller;

import com.bank.account_service.dto.*;
import com.bank.account_service.exception.AccountNotPresentException;
import com.bank.account_service.exception.AlreadyAppliedException;
import com.bank.account_service.model.Account;
import com.bank.account_service.service.AccountService;
import com.bank.authorization.security.ValidateToken;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/account")
@Slf4j
public class AccountController {
    @Autowired
    AccountService accountService;
    @Autowired
    ValidateToken validateToken;
    private static final String AUTH = "Authorization";

    @PostMapping("/apply")
    public ResponseEntity<ApiResponse> applyOnlineBanking(@RequestBody @Valid AccountTypeDto accountTypeDto, HttpServletRequest request) throws AlreadyAppliedException, AlreadyAppliedException {
        log.info(accountTypeDto.getAccountType());
        String token =request.getHeader(AUTH);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.successHandler(accountService.applyOnlineBanking(accountTypeDto,token.substring(7))));
    }

    @GetMapping("/get/all/account")
    public ResponseEntity<ApiResponse> getAllAccountsByUserId(HttpServletRequest request) throws AccountNotPresentException {
        String token =request.getHeader(AUTH).substring(7);
        String userId=validateToken.extractUserName(token);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.successHandler(accountService.getAllAccountsByUserID(userId)));
    }
    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Account createAccount(@RequestBody UserIdDto userIdDto){
        log.info("Account Controller: createAccount(-); Starts]");
        return accountService.createAccount(userIdDto);
    }

    @PutMapping("/kyc/initiate")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse> initiateKyc(@RequestBody AccountNumberDto accountNumberDto){
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.successHandler(accountService.initiateKyc(accountNumberDto)));
    }
    @PutMapping("/kyc/update")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<ApiResponse> updateKyc(HttpServletRequest request){
        String token=request.getHeader(AUTH);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.successHandler(accountService.updateKyc(token.substring(7))));
    }
    @PostMapping("/account-details/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<List<AccountDto>>> getAccountDetails(@PathVariable Integer userId) throws AccountNotPresentException {
        log.info("Transaction"+userId);
        List<AccountDto> accountDto = accountService.getAccountDetails(userId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.successHandler(accountDto));
    }
    @PostMapping("/accountDetails")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<ApiResponse<List<AccountDto>>> getAccountDetailsforUser(@RequestBody UserIdDto userIdDto) throws AccountNotPresentException {
        log.info("Transaction"+userIdDto.getUserId());
        List<AccountDto> accountDto = accountService.getAccountDetails(Integer.parseInt(userIdDto.getUserId()));
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.successHandler(accountDto));
    }


    @PostMapping("/account-details/accountNumber")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<ApiResponse> getAccountDetailsByAccountNumber(@RequestBody AccountNumberDto accountNumberDto) throws AccountNotPresentException {
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.successHandler(accountService.getAccountDetailsByAccountNumber(accountNumberDto)));
    }

    @PutMapping("/updated-balance")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<ApiResponse> updateAccountBalance(@RequestBody AccountBalanceWithTransactionCountDto accountBalanceWithTransactionCountDto){
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.successHandler(accountService.updateAccountBalanceAndTransactionCount(accountBalanceWithTransactionCountDto)));
    }

    @GetMapping("/all-accounts")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse> getAllAccounts(){
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.successHandler(accountService.getAllAccounts()));
    }
    @GetMapping("/get")
    @PreAuthorize("hasAuthority('USER')")
    public  ResponseEntity<ApiResponse> getAccountByToken(HttpServletRequest httpServletRequest){
        String token=httpServletRequest.getHeader(AUTH).substring(7);
        String userId=validateToken.extractUserName(token);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.successHandler(accountService.getAccountByToken(userId)));
    }
}
