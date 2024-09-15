package com.bank.account_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Account {
    @Id
    private String accountNumber;
    private String userId;
    private Double accountBalance;
    private String accountStatus;
    private String kyc;
    private Integer transactionCount;
    private String accountType;

}
