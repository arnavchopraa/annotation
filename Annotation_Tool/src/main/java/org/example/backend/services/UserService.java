package org.example.backend.services;

import org.example.backend.models.User;
import org.example.backend.models.LoginRequest;
import org.example.database.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
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
        if (repo.findById(id).isEmpty() ) {
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

    /**
     * Method for registering a new user
     *
     * @param loginRequest a login request entity, that contains the username and the password
     */
    public void registerUser(LoginRequest loginRequest) {
        if (loginRequest == null) {
            return;
        }
        if (repo.findById(loginRequest.getUsername()).isPresent()) {
            return;
        }
        // TODO - must change role
        repo.save(new User(loginRequest.getUsername(), loginRequest.getPassword(), "supervisor"));
    }

    /**
     * Method for authenticating a user
     *
     * @param loginRequest a login request entity, that contains the username and the password
     * @return true if the user is authenticated, false otherwise
     */
    public boolean authenticateUser(LoginRequest loginRequest) throws NoSuchAlgorithmException {
        // System.out.println(loginRequest.getUsername());
        User user = repo.findById(loginRequest.getUsername()).orElse(null);
        System.out.println(user.getPassword());
        System.out.println(PasswordHashingService.hashPassword(loginRequest.getPassword()));
        if (user == null) {
            return false;
        }
        // debug pass strings
        /*
        for(int i = 0;i < user.getPassword().length();i++)
            System.out.print((int) user.getPassword().charAt(i) + " ");
        System.out.println();
        String hashedpass = PasswordHashingService.hashPassword(loginRequest.getPassword());
        for(int i = 0;i < hashedpass.length();i++)
            System.out.print((int) hashedpass.charAt(i) + " ");
        System.out.println();*/

        return user.getPassword().equals(PasswordHashingService.hashPassword(loginRequest.getPassword()));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = repo.findById(username);
        if(user.isPresent()) {
            User userObj = user.get();
            return userObj;
        } else {
            throw new UsernameNotFoundException("User not found.");
        }
    }
}