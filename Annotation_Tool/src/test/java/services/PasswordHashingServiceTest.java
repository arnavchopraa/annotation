package services;

import org.example.backend.services.PasswordHashingService;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

class PasswordHashingServiceTest {

    private final PasswordEncoder passwordEncoder = new PasswordHashingService();

    @Test
    void testHashPassword() {
        String password = "mySecretPassword";
        String hashedPassword = PasswordHashingService.hashPassword(password);
        assertNotNull(hashedPassword);
        assertNotEquals(password, hashedPassword);
    }

    @Test
    void testEncode() {
        String password = "mySecretPassword";
        String hashedPassword = passwordEncoder.encode(password);
        assertNotNull(hashedPassword);
        assertNotEquals(password, hashedPassword);
    }

    @Test
    void testMatches() {
        String password = "mySecretPassword";
        String hashedPassword = passwordEncoder.encode(password);
        assertTrue(passwordEncoder.matches(password, hashedPassword));
    }

    @Test
    void testHashPasswordNoSuchAlgorithmException() {
        assertThrows(RuntimeException.class, () -> {
            PasswordHashingService.hashPassword(null);
        });
    }
}
