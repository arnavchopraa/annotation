package org.example.backend.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.ToString;
import org.example.backend.services.PasswordHashingService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Objects;

@Entity
@ToString
@Table(name="coordinators")
public class User implements UserDetails{
    @Id
    @Column(name="email")
    private String id;

    @Column(name="username")
    private String name;

    @Column(name="password")
    private String password;

    // supervisor / student / admin
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
        try {
            this.password = PasswordHashingService.hashPassword(password);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        this.role = role;
    }

    /**
     * AllArgsConstructor for User - Did not use lombok to specify that the password must
     * be hashed upon user creation.
     *
     * @param email User's email
     * @param name User's full name
     * @param password User's password, hashed using PasswordHashingService
     * @param role User's role in the application
     */
    public User(String email, String name, String password, String role) {
        this.id = email;
        this.name = name;
        try {
            this.password = PasswordHashingService.hashPassword(password);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
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

    @Override
    public String getUsername() {
        return id;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
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