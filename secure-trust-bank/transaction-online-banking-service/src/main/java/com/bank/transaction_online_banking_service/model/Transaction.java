package com.bank.transaction_online_banking_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue
    private int transactionId;
    private LocalDate transactionDate;
    private String transactionType;
    private String transactionDescription;

    @OneToMany(mappedBy = "transactions", cascade = CascadeType.ALL)
    private List<TransactionDetails> transactionDetailsList;


}
