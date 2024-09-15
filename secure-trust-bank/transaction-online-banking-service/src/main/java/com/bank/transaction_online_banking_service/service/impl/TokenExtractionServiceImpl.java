package com.bank.transaction_online_banking_service.service.impl;

import com.bank.authorization.security.ValidateToken;
import com.bank.transaction_online_banking_service.dto.TokenDetails;
import com.bank.transaction_online_banking_service.exception.InvalidException;
import com.bank.transaction_online_banking_service.service.TokenExtractionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class TokenExtractionServiceImpl implements TokenExtractionService {

    @Autowired
    private ValidateToken validateToken;
    @Override
    public TokenDetails extractToken(HttpServletRequest httpServletRequest) throws InvalidException {
        String authToken= httpServletRequest.getHeader("Authorization");
        String token=authToken.substring(7);
//        String accountNumber=validateToken.extractAccountNumber(token);
        String accountNumber="EMPTY";
        String role= validateToken.extractRole(token);
//        String accountStatus= validateToken.extractStatus(token);
        String accountStatus="GOOD";
        if(Objects.equals(accountNumber, "null")){
            throw new InvalidException("AccountNumber is empty, The Account is Inactive. Manager Approval is required ");
        }
        else if(accountStatus.equals("INACTIVE")){
            throw new InvalidException("Application Status is Inactive");
        }
        else {

            TokenDetails tokenDetails = new TokenDetails(accountNumber, accountStatus, role);
            return tokenDetails;
        }
    }
}