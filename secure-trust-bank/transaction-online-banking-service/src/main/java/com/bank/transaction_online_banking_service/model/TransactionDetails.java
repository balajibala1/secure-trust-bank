package com.bank.transaction_online_banking_service.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "transaction_details")
public class TransactionDetails {

    @Id
    @GeneratedValue
    private int transactionDetailsId;
    @ManyToOne
    @JoinColumn(name = "transaction_id")
    private Transaction transactions;
    private String accountNumber;

    private double debited;

    private double credited;
}
