package com.example.dripchip.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Component
public class JWTUtil {
    @Value("${app.secret.key}")
    private String secret_key;

    public String generateToken(String subject) {
        String tokenId= String.valueOf(new Random().nextInt(10000));
        return Jwts.builder()
                .setId(tokenId)
                .setSubject(subject)
                .setIssuer("ABC_Ltd")
                .setAudience("XYZ_Ltd")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(1)))
                .signWith(SignatureAlgorithm.HS512, Base64.getEncoder().encode(secret_key.getBytes()))
                .compact();
    }

    public Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(Base64.getEncoder().encode(secret_key.getBytes()))
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isValidToken(String token,String username) {
        String tokenUserName=getSubject(token);
        return (username.equals(tokenUserName) && !isTokenExpired(token));
    }

    public boolean isTokenExpired(String token) {
        return getExpirationDate(token).before(new Date(System.currentTimeMillis()));
    }

    private Date getExpirationDate(String token) {
        return getClaims(token).getExpiration();
    }

    public String getSubject(String token) {
        return getClaims(token).getSubject();
    }
}
