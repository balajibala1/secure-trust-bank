package com.bank.transaction_credit_card_service.controller;


import com.bank.transaction_credit_card_service.dto.ApiResponse;
import com.bank.transaction_credit_card_service.service.GenerateBillService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class GenerateBillController {
    private final GenerateBillService generateBillService;
    @PutMapping("/generate-bill")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<String>> generateBill(){
        String result = generateBillService.generateBill();
        System.out.println("The processId: for LoadBalancer for BillGenerate is: "+ProcessHandle.current().pid());
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.successHandler(result));
    }
}