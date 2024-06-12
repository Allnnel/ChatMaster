package org.example.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.example.exception.CustomException;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
@Component
public class JwtTokenProvider {

    private final SecretKey secretKey;
    public JwtTokenProvider() {
        String secretKeyString = "2yWX%8FhB1z!KQd@P3mZvfA$LgXnSjWn";
        // Преобразование строки в секретный ключ
        byte[] secretKeyBytes = secretKeyString.getBytes();
        this.secretKey = new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS512.getJcaName());
    }

    // Метод для генерации JWT токена на основе имени пользователя и роли
    public String generateToken(String login, String role) {
        Date now = new Date();
        // Рассчет времени истечения срока действия токена (24 часа после текущего времени)
        Date expiryDate = new Date(now.getTime() + 24 * 60 * 60 * 1000);
        // Генерация JWT токена с указанием подлежащего, времени создания и времени истечения срока действия
        return Jwts.builder()
                .claim("role", role) // Добавление роли в токен
                .setSubject(login)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(secretKey) // Подписание токена с использованием секретного ключа
                .compact(); // Компактное представление токена в виде строки
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
