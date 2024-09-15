package com.bank.user_service.controller;

import com.bank.user_service.dto.AccountDto;
import com.bank.user_service.dto.ApiResponse;
import com.bank.user_service.dto.ProspectApplicationDto;
import com.bank.user_service.exception.ApplicationNumberNotFoundException;
import com.bank.user_service.service.AdminService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class AdminController {
    @NonNull
    private AdminService adminService;
    @GetMapping("/application/status/pending")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<List<ProspectApplicationDto>>> getAllPendingApplications(@RequestParam String type) {
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.successHandler(adminService.getAllPendingApplications(type)));
    }
    @PutMapping("/{applicationNumber}/approve/credit-card")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<String>> approveCreditCardApplication(@PathVariable String applicationNumber) throws
            ApplicationNumberNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.successHandler(adminService.approvePendingCreditCardApplication
                (applicationNumber)));
    }
    @PutMapping("/{applicationNumber}/approve/online-banking")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<AccountDto>> approveOnlineBankingApplication(@PathVariable String applicationNumber) throws
            ApplicationNumberNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.successHandler(adminService.approvePendingOnlineBankingApplication
                (applicationNumber)));
    }
    @PutMapping("/{applicationNumber}/reject")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<String>> rejectApplication(@PathVariable String applicationNumber) throws
            ApplicationNumberNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.successHandler(adminService.rejectPendingApplication
                (applicationNumber)));
    }
}
