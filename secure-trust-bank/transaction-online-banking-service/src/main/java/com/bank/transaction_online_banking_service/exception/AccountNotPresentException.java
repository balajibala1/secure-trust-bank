package com.bank.transaction_online_banking_service.exception;

public class AccountNotPresentException extends Exception{
    public AccountNotPresentException(String message) {
        super(message);
    }
}