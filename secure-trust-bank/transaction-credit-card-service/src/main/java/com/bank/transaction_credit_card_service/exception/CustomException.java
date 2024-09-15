package com.bank.transaction_credit_card_service.exception;

public class CustomException extends Exception{

    public CustomException(String code) {
        super(code);
    }
}