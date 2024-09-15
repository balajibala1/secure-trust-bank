package com.bank.transaction_online_banking_service.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class TokenInterceptor implements RequestInterceptor {

    //public static final String SECRET="5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";
    public static final String SECRET = "Fskfo+3f6jDrc3fOeJ2vtiLoB+Pn/zNpBv1gSykXb0I0jPVH58uoSt/aBzPtw+eq";
    @Autowired
    private HttpServletRequest httpServletRequest;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        String serverToken = generateToken();
        requestTemplate.header("Authorization", "Bearer " + serverToken);
    }

    public String generateToken() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", "USER");
        claims.put("userId", "Null");
        return createToken(claims);
    }

    //    private String createToken(Map<String, Object> claims, String userName) {
//        String token= Jwts.builder()
//                .setClaims(claims)
//                .setSubject(userName)
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis()+1000*60*30))
//                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
//        System.out.println(token);
//        return token;
//    }
    private String createToken(Map<String, Object> claims) {
        return Jwts.builder().setClaims(claims).setSubject("null").setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + (1000 * 60 * 15))).signWith(getSignKey(),
                        SignatureAlgorithm.HS256).compact();
    }

    private Key getSignKey() {
        byte[] key = Decoders.BASE64.decode("Fskfo+3f6jDrc3fOeJ2vtiLoB+Pn/zNpBv1gSykXb0I0jPVH58uoSt/aBzPtw+eq");
        return Keys.hmacShaKeyFor(key);
    }
}