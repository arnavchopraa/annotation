package org.example.backend.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.example.backend.models.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String SECRET = "A88C8C0E10F080B8D5B836E4B97AE81197FECC13514C176370D518C741918C91";

    private static final long VALIDITY = TimeUnit.MINUTES.toMillis(30);

    /**
     * This method generates a jwt token for the user
     *
     * @param userDetails the user details
     * @return the generated token
     */
    public String generateToken(UserDetails userDetails) {
        // extra claims (information stored in the JWT)
        Map<String, String> claims = new HashMap<>();
        claims.put("role", ((User) userDetails).getRole());
        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusMillis(VALIDITY)))
                .signWith(generateKey())
                .compact();
    }
    /**
     * This method generates a key for the jwt token
     *
     * @return the generated key
     */
    private SecretKey generateKey() {
        byte[] decodedKey = Base64.getDecoder().decode(SECRET);
        return Keys.hmacShaKeyFor(decodedKey);
    }

    /**
     * This method extracts the username from the jwt token
     *
     * @param jwt the jwt token
     * @return the username
     */
    public String extractUsername(String jwt) {
        Claims claims = getClaims(jwt);
        return claims.getSubject();
    }

    /**
     * This method returns the claims from the jwt token
     *
     * @param jwt the jwt token
     * @return the claims
     */
    private Claims getClaims(String jwt) {
        return Jwts.parser()
                .verifyWith(generateKey())
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
    }

    /**
     * This method checks if the token is valid
     *
     * @param jwt the jwt token
     * @return true if the token is valid, false otherwise
     */
    public boolean isTokenValid(String jwt) {
        Claims claims = getClaims(jwt);
        return claims.getExpiration().after(Date.from(Instant.now()));
    }

    /**
     * This method extracts a claim from the jwt token
     *
     * @param token the jwt token
     * @param claimsResolver the claims resolver, required to extract the claims
     * @param <T> the type of the claim
     * @return the extracted claim
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * This method extracts the role from the jwt token
     *
     * @param token the jwt token
     * @return the role
     */
    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }
}
