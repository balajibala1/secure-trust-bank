package com.bank.account_service.exception;

public class AccountNotPresentException extends Exception{
    public AccountNotPresentException(String message) {
        super(message);
    }
}
