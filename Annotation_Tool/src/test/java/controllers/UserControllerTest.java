package controllers;

import org.example.backend.models.User;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class UserControllerTest {

    @Mock
    private UserService userService;

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
}