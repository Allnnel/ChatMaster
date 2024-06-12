package org.example.configuration;

import org.example.jwt.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authorize -> authorize
                        .antMatchers("/**").permitAll() // Включите ваш HTTP эндпоинт
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .cors().and()
                .csrf().disable();
    }


    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();

        // Разрешить использование куки
        config.setAllowCredentials(true);

        // Указать конкретные домены, с которых разрешены запросы
        config.addAllowedOrigin("http://example.com");
        config.addAllowedOrigin("https://example2.com");
        config.addAllowedOrigin("chrome-extension://pfdhoblngboilpfeibdedpjgfnlcodoo/index.html");
        config.addAllowedOrigin(" http://localhost:8081");

        // Разрешить все заголовки
        config.addAllowedHeader("*");

        // Разрешить все методы HTTP
        config.addAllowedMethod("OPTIONS");
        config.addAllowedMethod("HEAD");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("PATCH");

        // Регистрация конфигурации CORS для всех URL-адресов
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }

}
