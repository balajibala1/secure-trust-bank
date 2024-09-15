package com.bank.transaction_credit_card_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreditCardTransactionDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int transactionId;
    @NonNull
    private String creditCardNumber;
    private String transactionType;
    private String transactionStatus;
    private double amount;
    private LocalDate transactionDate;
    private int billNumber;
    @LastModifiedDate
    private LocalDate modifiedDate;
    @CreatedDate
    private LocalDate createdDate;
}