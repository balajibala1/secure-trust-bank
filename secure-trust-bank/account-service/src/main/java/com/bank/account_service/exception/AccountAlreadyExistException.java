package com.bank.account_service.exception;

public class AccountAlreadyExistException extends RuntimeException{


    public AccountAlreadyExistException(String message) {
        super(message);
    }

}
