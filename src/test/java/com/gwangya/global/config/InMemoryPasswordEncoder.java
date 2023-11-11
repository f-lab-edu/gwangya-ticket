package com.gwangya.global.config;

import org.springframework.security.crypto.password.PasswordEncoder;

public class InMemoryPasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence rawPassword) {
        return "{bcrypt} " + rawPassword;
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (encodedPassword.contains(rawPassword)) {
            return true;
        }
        return false;
    }
}
