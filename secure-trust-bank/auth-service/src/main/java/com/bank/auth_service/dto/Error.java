package com.bank.auth_service.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Error<T> {
    private T code;
    private T message;

}
