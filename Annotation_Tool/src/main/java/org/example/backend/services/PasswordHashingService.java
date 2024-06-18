package org.example.backend.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class PasswordHashingService implements PasswordEncoder {

    /**
     * Method for hashing a password
     *
     * @param password the password to be hashed
     * @return the hashed password
     * @throws RuntimeException if the hashing algorithm is not found
     */
    public static String hashPassword(String password) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("No such algorithm found");
        }
        md.update(password.getBytes());
        byte[] digest = md.digest();
        return new String(digest, Charset.forName("windows-1252"));
    }

    @Override
    public String encode(CharSequence rawPassword) {
        return hashPassword(rawPassword.toString());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encode(rawPassword.toString()).equals(encodedPassword);
    }
}
