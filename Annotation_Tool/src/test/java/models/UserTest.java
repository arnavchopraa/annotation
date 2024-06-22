package models;

import org.example.backend.services.PasswordHashingService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.example.backend.models.User;

import java.security.NoSuchAlgorithmException;


class UserTest {

    @Test
    void usernameGetterAndSetter() {
        User user = new User();
        user.setName("newUsername");
        assertEquals("newUsername", user.getName());
    }

    @Test
    void passwordGetterAndSetter() {
        User user = new User();
        user.setPassword("newPassword");
        assertEquals("newPassword", user.getPassword());
    }

    @Test
    void correctPassword() {
        User user = new User("username", PasswordHashingService.hashPassword("password123"), "supervisor");
        String hashed = PasswordHashingService.hashPassword("password123");
        assertTrue(user.checkPassword(hashed));
    }

    @Test
    void incorrectPassword() {
        User user = new User("username", "password123", "supervisor");
        assertFalse(user.checkPassword("password1234"));
    }

    @Test
    void roleGetterAndSetter() {
        User user = new User();
        user.setRole("supervisor");
        assertEquals("supervisor", user.getRole());
    }

    @Test
    void allArgsContructor() {
        User user = new User("email@email.com", "username", PasswordHashingService.hashPassword("password123"), "supervisor");
        String newPass = null;
        newPass = PasswordHashingService.hashPassword("password123");
        assertEquals("email@email.com", user.getId());
        assertEquals("username", user.getName());
        assertEquals(newPass, user.getPassword());
        assertEquals("supervisor", user.getRole());
    }

    @Test
    void noArgsContructor() {
        User user = new User();
        assertNull(user.getName());
        assertNull(user.getPassword());
        assertNull(user.getRole());
    }

    @Test
    void idGetterAndSetter() {
        User user = new User();
        user.setId("username");
        assertEquals("username", user.getId());
    }

    @Test
    void toStringTest() {
        User user = new User("username", PasswordHashingService.hashPassword("password123"), "supervisor");
        String newPass = null;
        newPass = PasswordHashingService.hashPassword("password123");
        assertEquals("User@" + Integer.toHexString(user.hashCode()) +
                ":[email=null,name=username,password=" + newPass + ",role=supervisor]", user.toString());
    }
}