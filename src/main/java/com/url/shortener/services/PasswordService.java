package com.url.shortener.services;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class PasswordService {

    public String hash(String password) {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }

    public boolean verify(String password, String actual) {
        try {
            return BCrypt.verifyer().verify(password.toCharArray(), actual.toCharArray()).verified;
        } catch (Exception ignored) {
            return false;
        }
    }
}