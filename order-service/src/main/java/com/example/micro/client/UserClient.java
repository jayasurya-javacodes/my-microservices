package com.example.micro.client;

import com.example.micro.dto.UserResponse;
import com.example.micro.exception.UserServiceUnavailableException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class UserClient {

    private final RestClient restClient;

    public UserClient(RestClient.Builder builder,
                      @Value("${user.service.url}") String userServiceUrl) {

        this.restClient = builder
                .baseUrl(userServiceUrl)
                .build();
    }

    public UserResponse getUserById(Long userId){

        try {

            return restClient.get()
                    .uri("/users/get/{id}", userId)
                    .retrieve()
                    .body(UserResponse.class);
        }catch (Exception e){
            throw new UserServiceUnavailableException("User Service is currently unavailable");
        }
    }
}
