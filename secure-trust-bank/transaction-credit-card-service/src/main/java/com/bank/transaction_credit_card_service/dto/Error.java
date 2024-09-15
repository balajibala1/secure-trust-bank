package com.bank.transaction_credit_card_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Error<T> {
    private T code;
    private T message;

}
