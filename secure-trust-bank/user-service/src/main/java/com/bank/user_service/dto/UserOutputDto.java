package com.bank.user_service.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserOutputDto {
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


}
