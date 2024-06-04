package services;

import org.example.exceptions.EmailException;
import org.example.services.EmailService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EmailServiceTest {
    EmailService emailService = new EmailService();

    @Test
    public void testRandomPassword() {
        for(int i = 0;i < 100;i++) {
            String s = emailService.generateRandomCode();
            assertEquals(8, s.length());
        }
    }
}
