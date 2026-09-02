package com.example.micro.service;

import com.example.micro.dto.UserResponse;
import com.example.micro.exception.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class UserService {

    private final Map<Long, UserResponse> users = new HashMap<>();

    public UserService() {

        users.put(
                1L,
                new UserResponse(
                        1L,
                        "Jayasurya",
                        "surya@mail.com"
                )
        );
        users.put(
                2L,
                new UserResponse(
                        2L,
                        "Mohan",
                        "mohan@mail.com"
                )
        );
    }

    public UserResponse getUserById(Long id) {
        UserResponse user = users.get(id);
        if (user == null) {
            throw new UserNotFoundException("User not found with id " + id);
        }

        int delay = ThreadLocalRandom.current().nextInt(1000, 3001);

        System.out.println("Delay: " + delay);

        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("User service Interrupted");
        }

        return user;
    }
}
