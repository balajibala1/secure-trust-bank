package com.bank.user_service.dto;

import lombok.Data;

@Data
public class Error<T> {
    private T errorCode;
    private Object msg;
}
