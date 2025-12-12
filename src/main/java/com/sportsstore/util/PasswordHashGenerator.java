package com.sportsstore.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHashGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);
        String password = "password123";
        String hash = encoder.encode(password);
        System.out.println("Password: " + password);
        System.out.println("BCrypt Hash (strength 10): " + hash);
        System.out.println("\nVerification test:");
        System.out.println("Does '" + password + "' match the hash? " + encoder.matches(password, hash));
    }
}
