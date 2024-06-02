package models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.example.backend.models.User;


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
        User user = new User("username", "password123", "supervisor");
        assertTrue(user.checkPassword("password123"));
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
}