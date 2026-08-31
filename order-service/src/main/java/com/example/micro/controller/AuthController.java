package com.example.micro.controller;
import com.example.micro.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public Map<String, String> login(
            @RequestParam String username,
            @RequestParam String password) {

        String token = authService.login(username, password);

        return Map.of("Token", token);
    }
}
