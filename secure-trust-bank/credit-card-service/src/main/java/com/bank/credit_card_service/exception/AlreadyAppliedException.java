package com.bank.credit_card_service.exception;

public class AlreadyAppliedException extends Exception{

    public AlreadyAppliedException(String code) {
        super(code);
    }
}