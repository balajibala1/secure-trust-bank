package com.bank.auth_service.service.impl;


import com.bank.auth_service.model.User;
import com.bank.auth_service.repository.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;

@Component
@Slf4j
public class JwtService {
    @Autowired
    private UserRepository userRepository;
    public String generateToken(String emailId) {
        Map<String, Object> claims = new HashMap<>();
        User user=userRepository.findByEmailId(emailId);
        if(user!=null){
            if(((user.getRole().equals("ADMIN"))) || ((user.getRole().equals("USER")) )) {
                claims.put("userId", user.getUserId());
                claims.put("role", user.getRole());
                log.info("Token generated for the user :"+user.getUserId());
                return createToken(claims, user.getEmailId());
            }else{
                throw new UsernameNotFoundException("UserNotFound");}
        }
        log.error("User not found for the id :"+emailId);
        throw new UsernameNotFoundException("User not found for the id :"+emailId);

    }
    private String createToken(Map<String, Object> claims, String emailId) {
        return Jwts.builder().setClaims(claims).setSubject(emailId).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + (1000 * 60 * 30))).signWith(getSignKey(),
                        SignatureAlgorithm.HS256).compact();
    }

    private Key getSignKey() {
        byte[] key= Decoders.BASE64.decode("Fskfo+3f6jDrc3fOeJ2vtiLoB+Pn/zNpBv1gSykXb0I0jPVH58uoSt/aBzPtw+eq");
        return Keys.hmacShaKeyFor(key);
    }

}