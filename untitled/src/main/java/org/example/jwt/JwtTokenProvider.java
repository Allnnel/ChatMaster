package org.example.jwt;

import io.jsonwebtoken.*;
import org.example.exception.CustomException;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Component
public class JwtTokenProvider {

    private final SecretKey secretKey;
    public JwtTokenProvider() {
        // Ваш секретный ключ (можно хранить в конфигурационном файле)
        String secretKeyString = "2yWX%8FhB1z!KQd@P3mZvfA$LgXnSjWn";

        // Преобразование строки в секретный ключ
        byte[] secretKeyBytes = secretKeyString.getBytes();
        this.secretKey = new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS512.getJcaName());
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
