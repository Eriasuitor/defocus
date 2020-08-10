package com.funtree.defocus.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.funtree.defocus.security.entity.Account;
import com.funtree.defocus.security.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class PasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    public PasswordAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        setFilterProcessesUrl("/users/login/password");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        Account account = null;
        try {
            account = new ObjectMapper().readValue(request.getInputStream(), Account.class);
            Authentication authentication = new UsernamePasswordAuthenticationToken(account.getUsername(), account.getPassword());
            return this.authenticationManager.authenticate(authentication);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        Account account = (Account) authResult.getPrincipal();
        Map<String, Object> claims = new HashMap<String, Object>();
        claims.put("subject", account.getId());
        claims.put("roles",
                account.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
        response.addHeader(jwtUtil.getTokenHeader(), jwtUtil.generateToken(claims));
    }
}
