package com.bank.transaction_online_banking_service.service;


import com.bank.transaction_online_banking_service.dto.TokenDetails;
import com.bank.transaction_online_banking_service.exception.InvalidException;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

public interface TokenExtractionService {
    TokenDetails extractToken(HttpServletRequest httpServletRequest) throws InvalidException;
}