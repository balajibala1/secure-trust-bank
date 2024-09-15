package com.bank.user_service.dto;

import lombok.*;

@Data
@AllArgsConstructor
public class FeignDto {
    private String applicationNumber;
    private String userId;
}
