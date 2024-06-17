package org.example.jwt;

import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import org.example.exception.CustomException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secretKeyString}")
    private String secretKeyString;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        this.secretKey = new SecretKeySpec(secretKeyString.getBytes(), SignatureAlgorithm.HS512.getJcaName());
    }


    // Метод для извлечения имени пользователя из JWT токена
    public String getUsernameFromToken(String token) throws CustomException {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return claims.getBody().getSubject();
        } catch (JwtException ex) {
            throw new CustomException("TOKEN_NOT_VALID", 404);
        }
    }

    // Метод для извлечения роли из JWT токена
    public String getRoleFromToken(String token) throws CustomException {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return (String) claims.getBody().get("role");
        } catch (JwtException ex) {
            throw new CustomException("TOKEN_NOT_VALID", 404);
        }
    }

}
