package com.bank.auth_service.controller;

import com.bank.auth_service.dto.ApiResponse;
import com.bank.auth_service.dto.AuthRequest;
import com.bank.auth_service.service.impl.JwtService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/bank")
public class AuthController {
    @Autowired
    private JwtService service;
    @Autowired
    private AuthenticationManager authenticationManager;
    @PostMapping("/login")
    public ResponseEntity<Object> generateToken(@Valid @RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken
                (authRequest.getEmailId(), authRequest.getPassword()));
        if(authentication.isAuthenticated()) {
            return new ResponseEntity<>(ApiResponse.successHandler(service.generateToken(authRequest.getEmailId())),
                    HttpStatus.CREATED);
        }
        throw new UsernameNotFoundException("You are not a registered User");
    }

}