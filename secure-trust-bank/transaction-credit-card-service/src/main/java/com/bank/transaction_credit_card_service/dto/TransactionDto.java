package com.bank.transaction_credit_card_service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Data
@NoArgsConstructor
public class TransactionDto {
    @Pattern(regexp = "[0-9]{16}", message = "Invalid Credit Card Number")
    @NotNull(message = "Card Number Cannot be null")
    private String creditCardNumber;
    @NotNull(message = "Amount Cannot be null")
    private Double amount;
    private int billNo;

}