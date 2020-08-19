package com.funtree.defocus.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.funtree.defocus.security.entity.Account;
import com.funtree.defocus.security.util.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class PasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public PasswordAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        setFilterProcessesUrl("/accounts/sign-in/password");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            Account account = new ObjectMapper().readValue(request.getInputStream(), Account.class);
            Authentication authentication = new UsernamePasswordAuthenticationToken(account.getUsername(), account.getPassword());
            return this.authenticationManager.authenticate(authentication);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) {
        Account account = (Account) authResult.getPrincipal();
        Map<String, Object> claims = new HashMap<>();
        claims.put("subject", account.getId());
        claims.put("roles", account.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
        response.addHeader(JwtUtil.getTokenHeader(), JwtUtil.generateToken(claims));
    }
}
