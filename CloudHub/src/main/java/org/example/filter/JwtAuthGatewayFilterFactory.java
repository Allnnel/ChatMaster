package org.example.filter;

import org.example.attribute.User;
import org.example.attribute.response.ResponseMessageObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static java.lang.System.out;

@Component
public class JwtAuthGatewayFilterFactory extends AbstractGatewayFilterFactory<JwtAuthGatewayFilterFactory.Config> {

    @Autowired
    private RestTemplate restTemplate;

    public JwtAuthGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        out.println("111111111111");
        return (exchange, chain) -> {
                HttpHeaders headers = exchange.getRequest().getHeaders();
                if (!headers.containsKey(HttpHeaders.AUTHORIZATION)) {
                    return Mono.error(new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "Missing authorization header"));
                }
                String authHeader = headers.getFirst(HttpHeaders.AUTHORIZATION);
                String url = "http://AUTH-SERVICE/api/auth/user?token=" + authHeader;
                try {
                    ResponseMessageObject responseMessageObject = restTemplate.getForObject(url, ResponseMessageObject.class);
                    if (responseMessageObject.getCode() != 200) {
                        return Mono.error(new HttpClientErrorException(HttpStatus.UNAUTHORIZED, responseMessageObject.getMessage()));
                    }
                    User user = (User) responseMessageObject.getObject();
                    String userRole = user.getRole();

                    // Проверка роли пользователя
                    Set<String> allowedRoles = new HashSet<>(Arrays.asList(config.getAllowedRoles()));
                    if (!allowedRoles.contains(userRole)) {
                        return Mono.error(new HttpClientErrorException(HttpStatus.FORBIDDEN, "Access denied. Insufficient role."));
                    }
                } catch (HttpClientErrorException ex) {
                    return Mono.error(new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "Unauthorized"));
                }
            return chain.filter(exchange);
        };
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
