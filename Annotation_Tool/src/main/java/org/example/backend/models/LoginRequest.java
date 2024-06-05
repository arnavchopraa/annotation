package org.example.backend.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    @JsonProperty("username")
    public String username;

    @JsonProperty("password")
    public String password;

    /**
     * Empty constructor for the LoginRequest
     */
    public LoginRequest() {
    }

    /**
     * Create a new LoginRequest
     *
     * @param username the username of the login request
     * @param password the password of the login request
     * @return a new LoginRequest
     */
    @JsonCreator
    public static LoginRequest create(@JsonProperty("username") String username, @JsonProperty("password") String password) {
        return new LoginRequest(username, password);
    }

    /**
     * Constructor for the LoginRequest with all arguments
     *
     * @param username the username of the login request
     * @param password the password of the login request
     */
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
