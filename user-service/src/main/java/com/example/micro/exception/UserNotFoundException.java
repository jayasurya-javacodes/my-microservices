package com.example.micro.exception;

public class UserNotFoundException  extends RuntimeException{
    public UserNotFoundException(String message){
         super(message);
    }
}
