package com.bank.account_service.model;

import jakarta.persistence.*;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.*;
import org.springframework.data.annotation.*;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProspectApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator ="application_id")
    @GenericGenerator(name="application_id",strategy = "com.bank.account_service.model.ApplicationNumberGenerator")
    private String applicationNumber;
    private String status;
    private String user;
    private String type;
    private String accountType;
    @CreatedDate
    private LocalDate createdDate;
    @LastModifiedDate
    private LocalDate modifiedDate;
    @CreatedBy
    private String createdName;
    @LastModifiedBy
    private String modifiedName;
}