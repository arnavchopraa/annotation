package services;

import org.example.database.UserRepository;
import org.example.backend.models.User;
import org.example.backend.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository repo;

    @InjectMocks
    private UserService service;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetUser() {
        User user = new User();
        user.setId("1");
        when(repo.findById("1")).thenReturn(Optional.of(user));

        User result = service.getUser("1");
        assertNotNull(result);
        assertEquals("1", result.getId());
    }

    @Test
    public void testGetUser_NotFound() {
        when(repo.findById("1")).thenReturn(Optional.empty());

        User result = service.getUser("1");
        assertNull(result);
    }

    @Test
    public void testGetUsers() {
        User user1 = new User();
        User user2 = new User();
        when(repo.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<User> results = service.getUsers();
        assertEquals(2, results.size());
    }

    @Test
    public void testAddUser() {
        User user = new User();
        user.setId("1");
        when(repo.save(any(User.class))).thenReturn(user);
        when(repo.findById("1")).thenReturn(Optional.empty());

        User result = service.addUser(user);
        assertNotNull(result);
        assertEquals("1", result.getId());
    }

    @Test
    public void testAddUser_NullInput() {
        User result = service.addUser(null);
        assertNull(result);
    }

    @Test
    public void testAddUser_AlreadyExists() {
        User user = new User();
        user.setId("1");
        when(repo.findById("1")).thenReturn(Optional.of(user));

        User result = service.addUser(user);
        assertNull(result);
    }

    @Test
    public void testUpdateUser() {
        User user = new User();
        user.setId("1");
        when(repo.save(any(User.class))).thenReturn(user);

        User result = service.updateUser(user);
        assertNotNull(result);
        assertEquals("1", result.getId());
    }

    @Test
    public void testUpdateUser_NullInput() {
        User result = service.updateUser(null);
        assertNull(result);
    }

    @Test
    public void testDeleteUser() {
        User user = new User();
        user.setId("1");
        when(repo.findById("1")).thenReturn(Optional.of(user));

        User result = service.deleteUser("1");
        assertNotNull(result);
        assertEquals("1", result.getId());
        verify(repo, times(1)).deleteById("1");
    }

    @Test
    public void testDeleteUser_NotFound() {
        when(repo.findById("1")).thenReturn(Optional.empty());

        User result = service.deleteUser("1");
        assertNull(result);
        verify(repo, never()).deleteById("1");
    }

    @Test
    public void testCheckPassword() {
        User user = new User();
        user.setId("1");
        user.setPassword("password");

        assertTrue(service.checkPassword(user, "password"));
    }

    @Test
    void testLoadUserByUsername() {
        // Mock repository response
        String username = "testUser";
        User mockUser = new User();
        mockUser.setId("1");
        mockUser.setUsername(username);
        mockUser.setPassword("password"); // Normally you wouldn't return the actual password
        mockUser.setRole("ROLE_USER");

        when(repo.findById(username)).thenReturn(Optional.of(mockUser));

        // Call the method
        UserDetails userDetails = service.loadUserByUsername(username);

        // Assertions
        assertNotNull(userDetails);
        assertEquals("1", userDetails.getUsername());
        assertEquals("password", userDetails.getPassword()); // This should ideally not be the real password

        verify(repo, times(1)).findById(username);
    }

    @Test
    void testLoadUserByUsernameUserNotFound() {
        // Mock repository response
        String username = "nonExistingUser";
        when(repo.findById(username)).thenReturn(Optional.empty());

        // Call the method and assert exception
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
                () -> service.loadUserByUsername(username));

        assertEquals("User not found.", exception.getMessage());

        verify(repo, times(1)).findById(username);
    }
}
