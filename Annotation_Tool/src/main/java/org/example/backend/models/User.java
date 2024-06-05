package org.example.backend.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.ToString;

//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Objects;

@Entity
@ToString
@AllArgsConstructor
@Table(name="coordinators")
public class User {
    @Id
    @Column(name="email")
    private String id;

    @Column(name="username")
    private String name;

    @Column(name="password")
    private String password;

    @Column(name="role")
    private String role;

    /**
     * Basic constructor for User
     *
     * @param name the name of the user
     * @param password the password of the user
     * @param role the role of the user
     */

    public User(String name, String password, String role) {
        this.name = name;
        //this.password = encoder.encode(password);
        this.password = password;
        this.role = role;
    }


    /**
        * Json Constructor for User with all arguments
        *
        * @param id the id of the user
        * @param name the name of the user
        * @param password the password of the user
        * @param role the role of the user
        * @return the user with all the arguments
        */
    @JsonCreator
    public static User create(@JsonProperty("email") String id, @JsonProperty("username") String name,
        @JsonProperty("password") String password, @JsonProperty("role") String role) {
        return new User(id, name, password, role);
    }

    /**
     * Basic constructor for User
     */
    public User() { }

    /**
     * Getter for the id of the user
     *
     * @return the id of the user
     */
    @JsonProperty("email")
    public String getId() {
        return id;
    }

    /**
     * Setter for the id of the user
     *
     * @param id the id of the user
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Getter for the name of the user
     *
     * @return the name of the user
     */
    @JsonProperty("username")
    public String getName() {
        return name;
    }

    /**
     * Setter for the name of the user
     *
     * @param name the name of the user
     */

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for the password of the user
     *
     * @return the password of the user
     */
    @JsonProperty("password")
    public String getPassword() {
        return password;
    }

    /**
     * Setter for the password of the user
     *
     * @param password the password of the user
     */

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Getter for the role of the user
     *
     * @return the role of the user
     */
    @JsonProperty("role")
    public String getRole() {
        return role;
    }

    /**
     * Setter for the role of the user
     *
     * @param role the role of the user
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Method for checking if the password is correct
     *
     * @param password the password to check
     * @return true if the password is correct, false otherwise
     */

    public boolean checkPassword(String password) {
        return Objects.equals(this.password, password);
    }
}