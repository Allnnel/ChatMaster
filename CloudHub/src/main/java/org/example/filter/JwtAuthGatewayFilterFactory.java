package org.example.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.example.attribute.response.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Arrays;
import io.jsonwebtoken.*;

@Component
public class JwtAuthGatewayFilterFactory extends AbstractGatewayFilterFactory<JwtAuthGatewayFilterFactory.Config> {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthGatewayFilterFactory.class);

    @Autowired
    private ObjectMapper objectMapper;

    private SecretKey secretKey;

    @Value("${jwt.secretKeyString}")
    private String secretKeyString;

    public JwtAuthGatewayFilterFactory() {
        super(Config.class);
    }

    @PostConstruct
    public void init() {
        this.secretKey = new SecretKeySpec(secretKeyString.getBytes(), SignatureAlgorithm.HS512.getJcaName());
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            try {
                HttpHeaders headers = exchange.getRequest().getHeaders();
                String token = headers.getFirst(HttpHeaders.AUTHORIZATION);
                if (token == null || !token.startsWith("Bearer ")) {
                    logger.info("Missing or invalid authorization header");
                    return Mono.error(new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "Missing or invalid authorization header"));
                }
                token = token.replace("Bearer ", "");
                logger.info("Auth Header: {}", token);
                try {
                    String userRole = getRoleFromToken(token);
                    logger.info("User Role: {}", userRole);
                    String[] requiredRoles = config.getAllowedRoles();
                    logger.info("Allowed Roles: {}", (Object) requiredRoles);
                    if (requiredRoles != null && requiredRoles.length > 0) {
                        boolean hasPermission = Arrays.asList(requiredRoles).contains(userRole);
                        if (!hasPermission) {
                            String json = objectMapper.writeValueAsString(new ResponseMessage("Failed", 403, "User does not have permission to access this resource", null));
                            return Mono.error(new HttpClientErrorException(HttpStatus.FORBIDDEN, json));
                        }
                    }
                } catch (JwtException e) {
                    String json = objectMapper.writeValueAsString(new ResponseMessage("Failed", 403, "Invalid token", null));
                    return Mono.error(new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, json));
                }
            } catch (Exception ex) {
                return Mono.error(ex);
            }
            return chain.filter(exchange);
        };
    }

    public String getUsernameFromToken(String token) throws JwtException {
        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token);
        return claims.getBody().getSubject();
    }

    public String getRoleFromToken(String token) throws JwtException {
        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token);
        return (String) claims.getBody().get("role");
    }

    public static class Config {
        private String[] allowedRoles;
        public String[] getAllowedRoles() {
            return allowedRoles;
        }
        public void setAllowedRoles(String[] allowedRoles) {
            this.allowedRoles = allowedRoles;
        }
    }

}
