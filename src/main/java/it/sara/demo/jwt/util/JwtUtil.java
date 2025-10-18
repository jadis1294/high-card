package it.sara.demo.jwt.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.Keys;
import it.sara.demo.exception.GenericException;

import java.util.Date;

public class JwtUtil {

    private final String secretKey = "your-secret-key";
    private final String expectedIssuer = "your-issuer";

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

            // Optional: check roles/permissions
            // String role = claims.get("role", String.class);

            return claims;

        } catch (JwtException e) {
            throw new GenericException(401, "Invalid JWT token");
        }
    }
}

