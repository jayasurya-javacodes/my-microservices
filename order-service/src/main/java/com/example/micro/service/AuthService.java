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

        if (username.equals("admin") && password.equals("admin123")) {

            return jwtUtil.generateToken("admin", "ADMIN");

        }

        if (username.equals("user") && password.equals("user123")) {
            return jwtUtil.generateToken("user", "USER");
        }

        if(username.equals("manager") && password.equals("manager123")) {
            return jwtUtil.generateToken("manager", "MANAGER");
        }

        throw new InvalidCredentialsException("Invalid username or password");

    }

}
