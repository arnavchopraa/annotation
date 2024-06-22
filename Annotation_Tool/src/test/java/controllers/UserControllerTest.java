package controllers;

import org.example.backend.importmodels.PageDetails;
import org.example.backend.models.User;
import org.example.backend.requestModels.PasswordsRequest;
import org.example.backend.services.JwtService;
import org.example.backend.services.PasswordHashingService;
import org.example.backend.services.UserService;
import org.example.backend.controllers.UserController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private JwtService jwtService;
    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetUsers() {
        List<User> users = Arrays.asList(
                new User("user1", "password1", "role1"),
                new User("user2", "password2", "role2")
        );

        when(userService.getUsers()).thenReturn(users);

        ResponseEntity<List<User>> response = userController.getUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(users, response.getBody());
    }

    @Test
    void testGetUserFound() {
        User user = new User("user1", "password1", "role1");

        when(userService.getUser("user1")).thenReturn(user);

        ResponseEntity<User> response = userController.getUser("user1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    void testGetUserNotFound() {
        when(userService.getUser("user1")).thenReturn(null);

        ResponseEntity<User> response = userController.getUser("user1");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testAddUserSuccess() {
        User user = new User("user1", "password1", "role1");

        when(userService.addUser(user)).thenReturn(user);

        ResponseEntity<User> response = userController.addUser(user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    void testAddUserFailure() {
        User user = new User("user1", "password1", "role1");

        when(userService.addUser(user)).thenReturn(null);

        ResponseEntity<User> response = userController.addUser(user);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testDeleteUserSuccess() {
        User user = new User("user1", "password1", "role1");

        when(userService.deleteUser("user1")).thenReturn(user);

        ResponseEntity<User> response = userController.deleteUser("user1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    void testDeleteUserNotFound() {
        when(userService.deleteUser("user1")).thenReturn(null);

        ResponseEntity<User> response = userController.deleteUser("user1");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetPageDetails_Success() {
        String token = "Bearer validtoken";
        String username = "testuser";
        String role = "user";

        when(jwtService.extractUsername(any(String.class))).thenReturn(username);
        when(jwtService.extractRole(any(String.class))).thenReturn(role);

        ResponseEntity<PageDetails> response = userController.getPageDetails(token);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        PageDetails pageDetails = response.getBody();
        assertNotNull(pageDetails);
        assertEquals(username, pageDetails.getEmail());
        assertEquals(role, pageDetails.getRole());
    }

    @Test
    void testGetPageDetails_InvalidToken() {
        String token = "Bearer invalidtoken";

        when(jwtService.extractUsername(any(String.class))).thenThrow(new RuntimeException("Invalid token"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userController.getPageDetails(token);
        });

        assertEquals("Invalid token", exception.getMessage());
    }

    @Test
    void testUpdatePassword_Success() {
        String userId = "user123";
        String oldPassword = "oldpassword";
        String newPassword = "newpassword";
        User user = new User();
        user.setId(userId);
        user.setPassword(PasswordHashingService.hashPassword(oldPassword));

        PasswordsRequest passwordsRequest = new PasswordsRequest(oldPassword, newPassword);

        when(userService.getUser(userId)).thenReturn(user);
        when(userService.checkPassword(eq(user), any(String.class))).thenReturn(true);
        when(userService.updateUser(any(User.class))).thenReturn(user);

        ResponseEntity<User> response = userController.updatePassword(userId, passwordsRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(userId, response.getBody().getId());
    }

    @Test
    void testUpdatePassword_UserNotFound() {
        String userId = "user123";
        PasswordsRequest passwordsRequest = new PasswordsRequest("oldpassword", "newpassword");

        when(userService.getUser(userId)).thenReturn(null);

        ResponseEntity<User> response = userController.updatePassword(userId, passwordsRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testUpdatePassword_InvalidPassword() {
        String userId = "user123";
        PasswordsRequest passwordsRequest = new PasswordsRequest("oldpassword", "newpassword");

        User user = new User();
        user.setId(userId);
        user.setPassword("hashedpassword");

        when(userService.getUser(userId)).thenReturn(user);
        when(userService.checkPassword(eq(user), any(String.class))).thenReturn(false);

        ResponseEntity<User> response = userController.updatePassword(userId, passwordsRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void testUpdatePassword_HashingError() {
        String userId = "user123";
        String oldPassword = "oldpassword";
        String newPassword = "newpassword";
        User user = new User();
        user.setId(userId);
        user.setPassword("hashedpassword");

        PasswordsRequest passwordsRequest = new PasswordsRequest(oldPassword, newPassword);

        when(userService.getUser(userId)).thenReturn(user);
        when(userService.checkPassword(eq(user), any(String.class))).thenReturn(true);
        doThrow(new RuntimeException("Hashing error")).when(userService).updateUser(any(User.class));

        ResponseEntity<User> response = userController.updatePassword(userId, passwordsRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}