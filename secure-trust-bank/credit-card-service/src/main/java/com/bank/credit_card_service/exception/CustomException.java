package com.bank.credit_card_service.exception;

public class CustomException extends Throwable {
    private static final long serialVersionUID = 1L;
    public CustomException(String msg) {
        super(msg);
    }
}