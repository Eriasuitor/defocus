package com.funtree.defocus.security.filter;

import com.funtree.defocus.security.util.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TokenAuthorizationFilter extends BasicAuthenticationFilter {
    public TokenAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        Authentication authentication = getAuthentication(request);
        if (authentication != null) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(JwtUtil.getTokenHeader());
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        Map<String, ?> claims = JwtUtil.getClaims(token.replace("Bearer ", ""));
        List<GrantedAuthority> authorities = ((List<String>) claims.get("roles"))
                .stream()
                .map(role -> new SimpleGrantedAuthority((String) role))
                .collect(Collectors.toList());
        return new UsernamePasswordAuthenticationToken(claims.get("subject"), null, authorities);
    }
}
