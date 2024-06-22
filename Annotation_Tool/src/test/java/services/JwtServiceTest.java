package services;

import io.jsonwebtoken.Jwts;
import jakarta.xml.bind.DatatypeConverter;
import org.example.backend.models.User;
import org.example.backend.services.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JwtServiceTest {

    @Test
    public void generateSecretKey() {
        SecretKey key = Jwts.SIG.HS256.key().build();
        String encodedKey = DatatypeConverter.printHexBinary(key.getEncoded());
        System.out.printf("\nKey: [%s]\n", encodedKey);
    }

    private JwtService jwtService;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        userDetails = mock(User.class);
        when(userDetails.getUsername()).thenReturn("testUser");
        when(((User) userDetails).getRole()).thenReturn("ROLE_USER");
    }

    @Test
    void testGenerateToken() {
        String token = jwtService.generateToken(userDetails);
        assertNotNull(token);
    }

    @Test
    void testExtractUsername() {
        String token = jwtService.generateToken(userDetails);
        String username = jwtService.extractUsername(token);
        assertEquals("testUser", username);
    }

    @Test
    void testIsTokenValid() {
        String token = jwtService.generateToken(userDetails);
        assertTrue(jwtService.isTokenValid(token));
    }

    @Test
    void testExtractRole() {
        String token = jwtService.generateToken(userDetails);
        String role = jwtService.extractRole(token);
        assertEquals("ROLE_USER", role);
    }


    @Test
    void testExpiredToken() {
        // Set up a token with a past expiration date
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", "ROLE_USER");
        String expiredToken = Jwts.builder()
                .setClaims(claims)
                .setSubject("testUser")
                .setIssuedAt(Date.from(Instant.now().minusMillis(TimeUnit.HOURS.toMillis(1))))
                .setExpiration(Date.from(Instant.now().plusMillis(TimeUnit.HOURS.toMillis(1))))
                .signWith(jwtService.generateKey())
                .compact();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                assertFalse(jwtService.isTokenValid(expiredToken));
            }
        }, 10L);

    }

}
