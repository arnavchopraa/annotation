package models;

import org.example.backend.models.LoginRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LoginRequestTest {
    @Test
    void shouldSetAndGetUsername() {
        LoginRequest loginRequest = new LoginRequest();
        String expectedUsername = "testUsername";
        loginRequest.setUsername(expectedUsername);
        assertEquals(expectedUsername, loginRequest.getUsername());
    }

    @Test
    void shouldSetAndGetPassword() {
        LoginRequest loginRequest = new LoginRequest();
        String expectedPassword = "testPassword";
        loginRequest.setPassword(expectedPassword);
        assertEquals(expectedPassword, loginRequest.getPassword());
    }

    @Test
    void allArgsConstructor() {
        String expectedUsername = "testUsername";
        String expectedPassword = "testPassword";
        LoginRequest loginRequest = new LoginRequest(expectedUsername, expectedPassword);
        assertEquals(expectedUsername, loginRequest.getUsername());
        assertEquals(expectedPassword, loginRequest.getPassword());
    }

    @Test
    void toStringTest(){
        String expectedUsername = "testUsername";
        String expectedPassword = "testPassword";
        LoginRequest loginRequest = new LoginRequest(expectedUsername, expectedPassword);
        String expectedString = "LoginRequest(username=" + expectedUsername + ", password=" + expectedPassword + ")";
        assertEquals(expectedString, loginRequest.toString());
    }
}
