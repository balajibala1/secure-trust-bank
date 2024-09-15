package com.bank.auth_service.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @Column(name = "user_id")
    private Integer userId;
    private String emailId;
    private String password;
    private String role;

}