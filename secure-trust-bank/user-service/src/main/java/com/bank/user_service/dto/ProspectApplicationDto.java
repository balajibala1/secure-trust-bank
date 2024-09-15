package com.bank.user_service.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProspectApplicationDto {
    private String applicationNumber;
    private String type;
    private String userId;
    private String firstName;
    private String lastName;
    private String panNumber;
    private String city;

}
