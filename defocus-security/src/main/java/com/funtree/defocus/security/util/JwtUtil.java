package com.funtree.defocus.security.util;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;
import java.util.Map;

public class JwtUtil {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);
    @Value("${jwt.signatureKey}")
    private static final String signatureKey = "*de*fau*l*t*_*s**ign**at***ur*e***_k***ey";
    @Value("${jwt.expiredIn}")
    private static final Long expiredIn = 200L;
    @Value("${jwt.tokenHeader}")
    private static final String tokenHeader = "Authorization";

    public static String generateToken(Map<String, Object> clams) {
        return Jwts.builder().signWith(Keys.hmacShaKeyFor(signatureKey.getBytes()))
                .setExpiration(new Date(System.currentTimeMillis() + expiredIn))
                .setClaims(clams)
                .compact();
    }

    public static Map<String, Object> getClaims(String token) {
        return Jwts.parser().setSigningKey(Keys.hmacShaKeyFor(signatureKey.getBytes()))
                .parseClaimsJws(token).getBody();
    }

    public static String getTokenHeader() {
        return tokenHeader;
    }
}
