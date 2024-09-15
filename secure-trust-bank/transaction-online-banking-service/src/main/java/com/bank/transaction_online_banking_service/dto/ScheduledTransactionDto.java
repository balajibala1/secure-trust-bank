package com.bank.transaction_online_banking_service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ScheduledTransactionDto {
    private int id;
    private String fromUserId;

    private String fromAccountNumber;
    @Pattern(regexp = "^[A-Za-z]{3}\\d{10}$", message = "Account number should contain alphanumeric and should " +
            "follow this 13 characters pattern ABCXXXXXXXXXX")
    @NotNull(message = "toAccountNumber must not be null")
    private String toAccountNumber;
    @NotNull  (message = "transferAmount must not be null")
    private double transferAmount;
    @NotNull (message = "scheduledOn must not be null")
    private String scheduledOn;
    private String executed;
    @JsonIgnore
    private Long transactionId;
}