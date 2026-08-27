package com.example.micro.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/config")
public class ConfigController {

    @Value("${app.message}")
    private String message;

    @Value("${app.timeout}")
    private String timeout;

    @Value("${app.environment}")
    private String environment;

    @GetMapping("/config-test")
    public String configTest() {
        return "Message: " + message
                + ", timeout: " + timeout
                + ", environment: " + environment;
    }


}
