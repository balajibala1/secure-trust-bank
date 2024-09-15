package com.bank.transaction_online_banking_service.configuration;

import com.bank.authorization.security.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
public class AuthConfig {
    @Autowired
    private JwtFilter jwtFilter;
    @Bean
    public SecurityFilterChain authorize(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.csrf(c->c.disable())
                .authorizeHttpRequests(authorizeHttpRequests-> authorizeHttpRequests.anyRequest()
                        .authenticated())
                .sessionManagement(sessionManagement-> sessionManagement.sessionCreationPolicy
                        (SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}