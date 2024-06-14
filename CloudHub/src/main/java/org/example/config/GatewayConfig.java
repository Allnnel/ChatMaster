package org.example.config;

import org.example.filter.JwtAuthGatewayFilterFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;

@Configuration
@EnableWebFlux
public class GatewayConfig {

    @Bean
    public JwtAuthGatewayFilterFactory jwtAuthGatewayFilterFactory() {
        return new JwtAuthGatewayFilterFactory();
    }
}
