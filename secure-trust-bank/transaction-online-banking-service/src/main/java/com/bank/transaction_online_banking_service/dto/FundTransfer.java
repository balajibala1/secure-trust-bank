package com.bank.transaction_online_banking_service.dto;

import jakarta.validation.constraints.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FundTransfer {
    @Pattern(regexp = "^[A-Z]{3}+[0-9]{10}$",message="please follow the pattern")
    private String fromAccountNumber;
    @Pattern(regexp = "^[A-Z]{3}+[0-9]{10}$",message="please follow the pattern")
    private String toAccountNumber;
    @NotNull(message = "transferAmount must not be null")
    @NotBlank
    private double transferAmount;

    @NotNull
    private String transferType;

    private LocalDateTime scheduledOn;
}
