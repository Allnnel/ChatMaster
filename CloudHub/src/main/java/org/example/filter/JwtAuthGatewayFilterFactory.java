package org.example.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.attribute.User;
import org.example.attribute.response.ResponseMessageObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpServerErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthGatewayFilterFactory extends AbstractGatewayFilterFactory<JwtAuthGatewayFilterFactory.Config> {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthGatewayFilterFactory.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestTemplate restTemplate;

    public JwtAuthGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            HttpHeaders headers = exchange.getRequest().getHeaders();
            if (!headers.containsKey(HttpHeaders.AUTHORIZATION)) {
                return Mono.error(new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "Missing authorization header"));
            }

            String token = headers.getFirst(HttpHeaders.AUTHORIZATION).replace("Bearer ", "");
            logger.info("Auth Header: {}", token);
            String url = "http://auth-service/api/auth/user?token=" + token;

            try {
                ResponseEntity<ResponseMessageObject> responseEntity = restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<ResponseMessageObject>() {}
                );

                ResponseMessageObject responseMessageObject = responseEntity.getBody();

                if (responseEntity.getStatusCode() != HttpStatus.OK || responseMessageObject.getCode() != 200) {
                    return Mono.error(new HttpClientErrorException(HttpStatus.UNAUTHORIZED, responseMessageObject.getMessage()));
                }

                User user = objectMapper.convertValue(responseMessageObject.getObject(), User.class);
                String userRole = user.getRole();
                logger.info("User Role: {}", userRole);

            } catch (ResourceAccessException e) {
                logger.error("Unable to connect to AUTH-SERVICE", e);
                return Mono.error(e);
            } catch (HttpClientErrorException ex) {
                logger.error("Unauthorized access", ex);
                return Mono.error(ex);
            } catch (HttpServerErrorException ex) {
                logger.error("Server error", ex);
                return Mono.error(ex);
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
