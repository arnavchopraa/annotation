package org.example.backend.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.ToString;

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

    /**
     * Basic constructor for User
     *
     * @param name the name of the user
     * @param password the password of the user
     */

    public User(String name, String password) {
        this.name = name;
        //this.password = encoder.encode(password);
        this.password = password;
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
     * Method for checking if the password is correct
     *
     * @param password the password to check
     * @return true if the password is correct, false otherwise
     */

    public boolean checkPassword(String password) {
        return Objects.equals(this.password, password);
    }
}