package it.sara.demo.jwt.auth;

import io.jsonwebtoken.Claims;
import it.sara.demo.exception.GenericException;
import it.sara.demo.jwt.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                Claims claims = jwtUtil.validateToken(token);
                // Puoi settare l'utente nel SecurityContext se necessario
            } catch (GenericException e) {
                // Gestione centralizzata tramite GlobalExceptionHandler
                request.setAttribute("jwt_error", e);
            }
        }

        filterChain.doFilter(request, response);
    }

}