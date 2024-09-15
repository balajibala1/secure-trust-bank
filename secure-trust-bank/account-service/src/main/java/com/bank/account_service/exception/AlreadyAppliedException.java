package com.bank.account_service.exception;

public class AlreadyAppliedException extends Exception{

    public AlreadyAppliedException(String code) {
        super(code);
    }
}