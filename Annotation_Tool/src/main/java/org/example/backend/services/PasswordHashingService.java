package org.example.backend.services;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHashingService {

    /**
     * Method for hashing a password
     *
     * @param password the password to be hashed
     * @return the hashed password
     * @throws NoSuchAlgorithmException if the hashing algorithm is not found
     */
    public static String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new NoSuchAlgorithmException("No such algorithm found");
        }
        md.update(password.getBytes());
        return new String(md.digest());
    }
}
