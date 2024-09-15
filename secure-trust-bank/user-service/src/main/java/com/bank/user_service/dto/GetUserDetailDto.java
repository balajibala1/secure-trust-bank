package com.bank.user_service.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetUserDetailDto {
    private Integer userId;
    private String accountType;

}
