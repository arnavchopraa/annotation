package org.example.backend.services;

import org.example.backend.models.User;
import org.example.database.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository repo;

    /**
     * Constructor for the UserService
     *
     * @param repo the repository for the user
     *             database
     */
    @Autowired
    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    /**
     * This method returns the user with the given id
     *
     * @param id the id of the user
     * @return the user with the given id
     */
    public User getUser(String id) {
        if ( repo.findById(id).isEmpty() ) {
            return null;
        }
        return repo.findById(id).get();
    }

    /**
     * This method returns all the users in the database
     *
     * @return a list of all the users in the database
     */
    public List<User> getUsers() {
        return Streamable.of(repo.findAll()).toList();
    }

    /**
     * This method adds a user to the database
     *
     * @param user the user to be added
     * @return the user that was added
     */
    public User addUser(User user) {
        // check if the user is null
        if (user == null) {
            return null;
        }
        // Check if the user already exists
        if (repo.findById(user.getId()).isPresent()) {
            return null;
        }
        return repo.save(user);
    }

    /**
     * This method updates a user in the database
     *
     * @param user the user to be updated
     * @return the user that was updated
     */
    public User updateUser(User user) {
        // check if the user is null
        if (user == null) {
            return null;
        }
        return repo.save(user);
    }

    /**
     * This method deletes a user from the database
     *
     * @param id the id of the user to be deleted
     * @return the user that was deleted
     */

    public User deleteUser(String id) {
        User deleted = repo.findById(id).orElse(null);
        if (repo.findById(id).isEmpty()) {
            return null;
        }
        repo.deleteById(id);
        return deleted;
    }
}