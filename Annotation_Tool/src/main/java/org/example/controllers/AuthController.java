package org.example.controllers;

import org.example.models.LoginRequest;
import org.example.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.example.services.UserService;

import java.security.NoSuchAlgorithmException;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;

    /**
     * Constructor for the AuthController
     *
     * @param userService the userService instance
     */
    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /**
     * This method is used to authenticate a user if the password is correct
     *
     * @param loginRequest the login request
     * @return A response entity with the user's role if the user is authenticated
     * @throws NoSuchAlgorithmException if the password encryption service fails
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) throws NoSuchAlgorithmException {
        boolean authenticated = userService.authenticateUser(loginRequest);
        User user = userService.getUser(loginRequest.getUsername());
        if (authenticated) {
            return ResponseEntity.ok(user.getRole());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }
}