package com.bank.transaction_credit_card_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class CreditCardDto {
    private Integer creditCardId;
    private String creditCardNumber;
    private Double creditCardLimit;
    private Double availableLimit;
    private String applicationNumber;
}
