package com.example.micro.service;

import com.example.micro.exception.InvalidCredentialsException;
import com.example.micro.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtUtil jwtUtil;

    public String login(String username, String password) {

        if ("admin".equals(username) && "admin123".equals(password)) {

            return jwtUtil.generateToken(username);
        }

        throw new InvalidCredentialsException(
                "Invalid username/password"
        );
    }
}
