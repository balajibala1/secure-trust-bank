package com.bank.user_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "user_id")
    private Integer userId;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String dateOfBirth;
    private String panNumber;
    private String address;
    private String street;
    private String city;
    private String pinCode;
    private String gender;
    private String emailId;
    private String password;
    private String role;
    @CreatedBy
    private String createdName;
    @CreatedDate
    private LocalDate createdDate;

}
