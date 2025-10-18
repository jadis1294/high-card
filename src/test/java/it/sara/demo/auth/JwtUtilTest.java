package it.sara.demo.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import it.sara.demo.exception.GenericException;
import it.sara.demo.jwt.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class JwtUtilTest {

    private JwtUtil jwtUtil;
    private static final String SECRET_KEY = "01234567890123456789012345678901"; // 32 bytes = 256 bits
    private static final String VALID_ISSUER = "your-issuer";

    private String generateValidToken() {
        return Jwts.builder()
                .setSubject("test-user")
                .setIssuer(VALID_ISSUER)
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 10)) // 10 minuti
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .compact();
    }

    private String generateTokenWithWrongIssuer() {
        return Jwts.builder()
                .setSubject("test-user")
                .setIssuer("invalid-issuer")
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 10))
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .compact();
    }

    private String generateExpiredToken() {
        return Jwts.builder()
                .setSubject("test-user")
                .setIssuer(VALID_ISSUER)
                .setExpiration(new Date(System.currentTimeMillis() - 1000 * 60)) // già scaduto
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .compact();
    }

    @BeforeEach
    void setup() {
        jwtUtil = JwtUtil.forTests(SECRET_KEY, VALID_ISSUER);
    }

    @Test
    void shouldValidateValidToken() throws GenericException {
        String token = generateValidToken(); // metodo helper
        Claims claims = jwtUtil.validateToken(token);
        assertEquals("your-issuer", claims.getIssuer());
    }

    @Test
    void shouldThrowExceptionForInvalidIssuer() {
        String token = generateTokenWithWrongIssuer();
        assertThrows(GenericException.class, () -> jwtUtil.validateToken(token));
    }

    @Test
    void shouldThrowExceptionForExpiredToken() {
        String token = generateExpiredToken();
        assertThrows(GenericException.class, () -> jwtUtil.validateToken(token));
    }



}
