package org.example.backend.controllers;

import org.example.backend.requestModels.LoginRequest;
import org.example.backend.services.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.example.backend.services.UserService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;
    /**
     * Constructor for the AuthController
     *
     * @param userService the userService instance
     * @param authenticationManager the authentication manager used for authentication
     * @param jwtService the jwt service used for authentication
     */
    @Autowired
    public AuthController(UserService userService, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    /**
     * This method is used to authenticate a user if the password is correct
     *
     * @param loginRequest the login request
     * @return A response entity with the user's role if the user is authenticated
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        if(loginRequest == null || loginRequest.getUsername() == null || loginRequest.getPassword() == null)
            return new ResponseEntity<>("Request body is malformed", HttpStatus.BAD_REQUEST);
        else if(loginRequest.getUsername().equals(""))
            return new ResponseEntity<>("Username field cannot be empty. Please try again.", HttpStatus.BAD_REQUEST);
        else if(loginRequest.getPassword().equals(""))
            return new ResponseEntity<>("Password field cannot be empty. Please try again.", HttpStatus.BAD_REQUEST);
        try {
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(), loginRequest.getPassword()));
            if (auth.isAuthenticated()) {
                String token = jwtService.generateToken(userService.loadUserByUsername(loginRequest.getUsername()));
                return ResponseEntity.ok(token);
            } else {
                throw new UsernameNotFoundException("No username");
            }
        } catch(AuthenticationException e) {
            return new ResponseEntity<>("Username and password combination are incorrect. Please try again.", HttpStatus.BAD_REQUEST);
        }
    }
}