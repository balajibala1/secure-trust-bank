package com.bank.user_service.dto;

import com.bank.user_service.validation.UniqueValue;
import com.bank.user_service.validation.ValidAge;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    @Id
    private Integer userId;
    @Pattern(regexp = "^[a-zA-Z' ]*$",message = "Please Enter Only AlphaNumberic Values")
    private String firstName;
    @NotBlank(message = "Please Enter LastName")
    @Pattern(regexp = "^[a-zA-Z' ]*$",message = "Please Enter Only AlphaNumberic Values")
    private String lastName;

    @NotBlank(message = "Please Enter PhoneNumber")
    @Pattern(regexp = "^[0-9]{10}$",message = "Enter PhoneNumber should be 10 Digits")
    @UniqueValue(field = "phoneNumber",message = "PhoneNumber is Already Present")
    private String phoneNumber;
    @NotBlank(message = "Please Enter DateOfBirth")
    @Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/(19|20)\\d{2}$",message = "Please Enter DatOfBirth in The format DD/MM/YYYY")
    @ValidAge(message = "Enter the format",ageMessage = "Age must be greater than 18")
    private String dateOfBirth;
    @NotBlank(message = "Please Enter PanNumber")
    @Pattern(regexp = "^[A-Z]{5}[0-9]{4}[A-Z]$",message = "Please Enter valid PanNumber")
    @UniqueValue(field = "panNumber",message = "PanNumber is Already Present")
    private String panNumber;
    @NotBlank(message = "Please Enter Address")
    private String address;
    @NotBlank(message = "Please Enter street")
    private String street;
    @NotBlank(message = "Please Enter city")
    private String city;
    @NotBlank(message = "Please Enter Pincode")
    @Pattern(regexp = "^[0-9]{6}$",message = "Please Enter valid Pincode")
    private String pincode;
    @NotBlank(message = "Please Enter Gender")
    private String gender;
    @NotBlank(message = "Please Enter EmailId")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+.com$",message = "Please Enter valid EmailId")
    @UniqueValue(field = "emailId",message = "EmailId is Already Present")
    private String emailId;
    @NotBlank(message = "Please Enter Password")
    private String password;
    private String role;

}
