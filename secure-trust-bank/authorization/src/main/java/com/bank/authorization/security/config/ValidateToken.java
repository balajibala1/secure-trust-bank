package com.bank.authorization.security.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class ValidateToken {
    private static final String JWT_SECRET = "Eskf©+3f6jDrc3f0eJ2vtiLoB+Pn/zNpBv1gSykXb0I0jPVH58uoSt/aBzPtw+eq";

    public String extractUserName(String token) {
        return extractAllClaims(token).get("userId").toString();
    }

    public Date getExpirationDate(String token) {
        return extractAllClaims(token).getExpiration();
    }

    public String extractRole(String token) {
        return extractAllClaims(token).get("role").toString();
    }

    public Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignkey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    Key getSignkey() {
        byte[] keys = Decoders.BASE64.decode(JWT_SECRET);
        return Keys.hmacShaKeyFor(keys);
    }

    public boolean isTokenExpired(String token) {
        return getExpirationDate(token).before(new Date());
    }

    public boolean tokenValidation(String token) {
        return !isTokenExpired(token);
    }
}
