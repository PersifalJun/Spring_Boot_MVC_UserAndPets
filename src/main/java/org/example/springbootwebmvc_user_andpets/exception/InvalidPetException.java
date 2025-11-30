package org.example.springbootwebmvc_user_andpets.exception;

public class InvalidPetException extends RuntimeException {
    public InvalidPetException(String message) {
        super(message);
    }
}
