package com.bank.authorization.security.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.security.auth.login.AccountNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private final ValidateToken validateToken;

    @Autowired
    public JwtFilter(ValidateToken validateToken) {
        this.validateToken = validateToken;
    }

    @Autowired
    @Qualifier("handleExceptionResolver")
    private HandlerExceptionResolver exceptionResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        if (requestURI.matches("/user/register")) {
            filterChain.doFilter(request, response);
            return;
        }
        String authHead = request.getHeader("Authorization");
        String token;
        String userName;
        try {
            if (authHead != null && authHead.startsWith("Bearer")) {
                token = authHead.substring(7);
                userName = validateToken.extractUserName(token);
                if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    List<SimpleGrantedAuthority> roles = Arrays.stream(validateToken.extractRole(token).split(","))
                            .map(SimpleGrantedAuthority::new).collect(Collectors.toList());
                    UserDetails user = new User(userName, "password", roles);
                    UsernamePasswordAuthenticationToken authToken;
                    if (validateToken.tokenValidation(token)) {
                        authToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                        System.out.println(SecurityContextHolder.getContext().getAuthentication());
                    }
                    if (authHead == null) {
                        throw new AccountNotFoundException("No Token is available");
                    }
                    filterChain.doFilter(request, response);
                }
            }
        } catch (Exception exception) {
            System.out.println(exception);
            exceptionResolver.resolveException(request, response, null, exception);
        }
    }
}
