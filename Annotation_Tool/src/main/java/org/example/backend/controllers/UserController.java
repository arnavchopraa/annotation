package org.example.backend.controllers;

import org.example.backend.models.User;
import org.example.backend.requestModels.PasswordsRequest;
import org.example.backend.services.PasswordHashingService;
import org.example.backend.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/users")
public class UserController {
    private UserService service;

    /**
     * Constructor for the UserController
     *
     * @param service the service for the user
     */
    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    /**
     * This method returns all the users in the database
     *
     * @return a list of all the users in the database
     */
    @GetMapping("/")
    @ResponseBody
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok(service.getUsers());
    }

    /**
     * This method returns the user with the given id
     *
     * @param id the id of the user
     * @return the user with the given id
     */
    @GetMapping("/{id}")

    public ResponseEntity<User> getUser(@PathVariable("id") String id) {
        User user = service.getUser(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    /**
     * This method adds a user to the database
     *
     * @param user the user to be added
     * @return the user that was added
     */
    @PostMapping("/")
    @ResponseBody
    public ResponseEntity<User> addUser(@RequestBody User user) {
        try {
            user.setPassword(PasswordHashingService.hashPassword(user.getPassword()));
        } catch (NoSuchAlgorithmException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        User user1 = service.addUser(user);

        if (user1 == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(user1);
    }

    /**
     * This method updates a user in the database
     *
     * @param user the user to be updated
     * @return the user that was updated
     */
    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        User user1 = service.updateUser(user);
        if (user1 == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(user1);
    }

    /**
     * This method deletes a user from the database
     *
     * @param id the id of the user to be deleted
     * @return the user that was deleted
     */
    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<User> deleteUser(@PathVariable String id) {
        User deleted = service.deleteUser(id);
        if (deleted == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(deleted);
    }

    /**
     * This method updates the password of a user
     * @param id the id of the user to be changed
     * @param pass An object containing the old and new password
     * @return the new updated user
     */
    @PutMapping("/updatePassword/{id}")
    @ResponseBody
    public ResponseEntity<User> updatePassword(@PathVariable String id, @RequestBody PasswordsRequest pass) {

        String oldPassword = pass.getOldPassword();
        String newPassword = pass.getNewPassword();

        User user = service.getUser(id);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        try {
            if(!service.checkPassword(user, PasswordHashingService.hashPassword(oldPassword)))
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } catch (NoSuchAlgorithmException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        try {
            user.setPassword(PasswordHashingService.hashPassword(newPassword));
        } catch (NoSuchAlgorithmException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        User updated = service.updateUser(user);
        return ResponseEntity.ok(updated);
    }

}