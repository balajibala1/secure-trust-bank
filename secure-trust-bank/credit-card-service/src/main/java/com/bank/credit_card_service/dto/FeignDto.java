package com.bank.credit_card_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class FeignDto {
    private String applicationNumber;
    private String userId;
}
