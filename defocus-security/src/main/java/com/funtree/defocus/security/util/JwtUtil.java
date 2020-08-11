package com.funtree.defocus.security.util;


import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;
import java.util.Map;

public class JwtUtil {
    private final Logger logger = LoggerFactory.getLogger(JwtUtil.class);
    @Value("${jwt.signatureKey}")
    private final String signatureKey = "*de*fau*l*t*_*s**ign**at***ur*e***_k***ey";
    @Value("${jwt.expiredIn}")
    private final Long expiredIn = 200L;
    @Value("${jwt.tokenHeader}")
    private final String tokenHeader = "Authorization";

    public String generateToken(Map<String, Object> clams) {
        return Jwts.builder().signWith(Keys.hmacShaKeyFor(this.signatureKey.getBytes()))
                .setExpiration(new Date(System.currentTimeMillis() + this.expiredIn))
                .setClaims(clams)
                .compact();
    }

    public Map<String, Object> getClaims(String token) {
        return Jwts.parser().setSigningKey(Keys.hmacShaKeyFor(this.signatureKey.getBytes()))
                .parseClaimsJws(token).getBody();
    }

    public String getTokenHeader() {
        return this.tokenHeader;
    }
}
