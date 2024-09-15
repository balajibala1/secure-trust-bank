package com.bank.auth_service.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequest {

    @Email(regexp = "^[A-Za-z0-9+_.-]+@(.+)+.com$",message = "Invalid email")
    @NotBlank(message = "Enter email")
    private String emailId;

    @NotNull(message = "Password Required")
    @NotBlank(message = "Enter password")
    private String password;

}
