package com.bank.user_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
public class ProspectApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator ="application_id")
    @GenericGenerator(name="application_id",strategy = "com.bankapplication.userservice.validation.ApplicationNumberGenerator")
    private String applicationNumber;
    private String status;
    private String type;
    private String user;
    @CreatedDate
    private LocalDate createdDate;
    @LastModifiedDate
    private LocalDate modifiedDate;
    @CreatedBy
    private String createdName;
    @LastModifiedBy
    private String modifiedName;
}
