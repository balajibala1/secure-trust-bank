package com.bank.credit_card_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProspectApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator ="application_id")
    @GenericGenerator(name="application_id",strategy = "com.bank.credit_card_service.model.ApplicationNumberGenerator")
    private String applicationNumber;
    private String status;
    private String user;
    private String type;
    @CreatedDate
    private LocalDate createdDate;
    @LastModifiedDate
    private LocalDate modifiedDate;
    @CreatedBy
    private String createdName;
    @LastModifiedBy
    private String modifiedName;
}