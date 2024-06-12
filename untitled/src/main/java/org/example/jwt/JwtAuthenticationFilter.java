package org.example.jwt;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.dto.SenderDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static java.lang.System.out;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter { // гарантирует единоразовый вызов фильтра для одного запроса

    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        try {
            // Извлечение токена из запроса
            String token = extractTokenFromRequest((HttpServletRequest) request);
            out.println(token);
            if (token != null) {
                String role = jwtTokenProvider.getRoleFromToken(token);
                out.println(role);
                String userName = jwtTokenProvider.getUsernameFromToken(token);
                out.println(userName);
                SenderDto user = new SenderDto(userName, role);
                List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("ROLE_" + user.getRole());
                Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                logger.info("User {} successfully authenticated with roles: {}" + user.getName() + " " + authorities);
            }
            filterChain.doFilter(request, httpServletResponse);
        } catch (Exception ex) {
            handleAuthenticationError(request,httpServletResponse, ex);
        }
    }

    private void handleAuthenticationError(HttpServletRequest request, HttpServletResponse response, Exception ex) throws IOException {
        logger.error("Authentication error: " + ex.getMessage() + " for URL: " + request.getRequestURL());
        if (response != null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");

            ObjectMapper objectMapper = new ObjectMapper();
            String errorMessage = objectMapper.writeValueAsString(Map.of(
                    "result", "Failed",
                    "message", ex.getMessage(),
                    "errorCode", "401"
            ));

            response.getWriter().write(errorMessage);
        }
    }



    private String extractTokenFromRequest(HttpServletRequest request) {
        String token = request.getParameter("token");
        if (token == null) {
            token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token =  token.substring(7);
            }
        }
        if (token != null && !token.isEmpty()) {
            return token;
        }
        return null;
    }


}

