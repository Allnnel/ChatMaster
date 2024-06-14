package org.example.security;

//import org.example.security.jwt.JwtAuthenticationFilter;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static java.lang.System.out;

//@EnableWebSecurity
public class SecurityConfig {
//    @Autowired
//    private JwtAuthenticationFilter jwtAuthenticationFilter;
//
//    @Bean
//    public HttpSecurity securityFilterChain(HttpSecurity http) throws Exception {
//        out.println("111111");
//        http
//                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF protection
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers(HttpMethod.POST, "/api/auth/register/phone").permitAll()
//                        .requestMatchers(HttpMethod.PUT, "/api/auth/refresh-token").permitAll()
//                        .requestMatchers(HttpMethod.POST,"/api/v1/find").hasAnyAuthority("EMPLOYEE")
//                )
//                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
//                .csrf(csrf->csrf.disable()); // Add JWT authentication filter
//
//        return http;
//    }


}