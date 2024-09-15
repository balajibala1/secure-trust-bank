package com.bank.user_service.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CreditCardInterceptor implements RequestInterceptor {
    private HttpServletRequest httpServletRequest;
    public CreditCardInterceptor(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }
    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header("Authorization",httpServletRequest.getHeader("Authorization"));
    }
}
