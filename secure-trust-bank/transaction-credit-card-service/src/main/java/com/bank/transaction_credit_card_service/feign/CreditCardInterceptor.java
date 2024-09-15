package com.bank.transaction_credit_card_service.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class CreditCardInterceptor implements RequestInterceptor {
    private final HttpServletRequest httpServletRequest;

    public CreditCardInterceptor(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header("Authorization",httpServletRequest.getHeader("Authorization"));

    }
}