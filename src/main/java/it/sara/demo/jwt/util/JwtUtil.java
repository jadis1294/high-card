package it.sara.demo.jwt.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import it.sara.demo.exception.GenericException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    private final String secretKey;
    private final String expectedIssuer;

    // Constructor for Spring; reads properties if available, otherwise falls back to safe defaults
    public JwtUtil(@Value("${app.jwt.secret:01234567890123456789012345678901}") String secretKey,
                   @Value("${app.jwt.issuer:your-issuer}") String expectedIssuer) {
        this.secretKey = secretKey;
        this.expectedIssuer = expectedIssuer;
    }

    // Helper factory for tests
    public static JwtUtil forTests(String secretKey, String expectedIssuer) {
        return new JwtUtil(secretKey, expectedIssuer);
    }

    public Claims validateToken(String token) throws GenericException {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            if (!expectedIssuer.equals(claims.getIssuer())) {
                throw new GenericException(401, "Invalid token issuer");
            }

            if (claims.getExpiration().before(new Date())) {
                throw new GenericException(401, "Token expired");
            }

            return claims;

        } catch (JwtException e) {
            throw new GenericException(401, "Invalid JWT token");
        }
    }
}

